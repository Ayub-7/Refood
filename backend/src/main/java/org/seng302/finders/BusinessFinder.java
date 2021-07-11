package org.seng302.finders;

import org.seng302.models.Business;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BusinessFinder {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaQuery<Business> criteriaQuery;
    private CriteriaBuilder criteriaBuilder;
    private Root<Business> businessRoot;

    private ArrayList<String> searchQueryKeywords(String query) {
        ArrayList<String> terms = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\"]\\S*|\"[^\"]*+\")\\s*").matcher(query);
        while (matcher.find()) {
            terms.add(matcher.group().replace("\"", ""));
        }
        return terms;
    }

    private Predicate criteriaBuilder(String term, boolean isLike) {
        //Obtains criteria
        if (isLike) {
            String[] subTerms = term.split(" ");
            List<Predicate> subTermPredicates = new ArrayList<>();

            for (String subTerm : subTerms) {
                subTerm = subTerm.strip().toLowerCase();
                Predicate criteria = criteriaBuilder.equal(criteriaBuilder.lower(businessRoot.get("name")), subTerm);
                subTermPredicates.add(criteria);
            }

            Predicate combinedCriteria = subTermPredicates.get(0);
            for (int i = 1; i < subTerms.length; i++) {
                combinedCriteria = criteriaBuilder.and(combinedCriteria, subTermPredicates.get(i));
            }

            return combinedCriteria;
        } else {
            term = term.strip().toLowerCase();
            return criteriaBuilder.like(criteriaBuilder.lower(businessRoot.get("name")), "%" + term + "%");
        }
    }

    private List<Business> queryProcess(ArrayList<String> terms, boolean isLike) {
        List<Predicate> criteriaList = new ArrayList<>();
        Logic logic = Logic.NONE;
        short consecutive = 0;
        for (String term : terms) {

            if (term.strip().equals("AND")) {
                logic = Logic.AND;
                consecutive = 0;
            }
            else if (term.strip().equals("OR")) {
                logic = Logic.OR;
                consecutive = 0;
            }
            else { // Not a logic operator.
                if (consecutive == 1) { // Automatically assume two terms next to each other is AND.
                    consecutive = 0;
                    logic = Logic.AND;
                } else {
                    consecutive++;
                }

                Predicate combinedCriteria = this.criteriaBuilder(term, isLike);

                if (criteriaList.isEmpty()) {
                    logic = Logic.NONE;
                    criteriaList.add(combinedCriteria);
                }
                else if (logic == Logic.AND) {
                    Predicate predicate = criteriaBuilder.and(criteriaList.get(0), combinedCriteria);
                    criteriaList.remove(0);
                    criteriaList.add(predicate);
                    logic = Logic.NONE;

                }
                else if (logic == Logic.OR) {
                    Predicate predicate = criteriaBuilder.or(criteriaList.get(0), combinedCriteria);
                    criteriaList.remove(0);
                    criteriaList.add(predicate);
                    logic = Logic.NONE;
                }
                else {
                    criteriaList.add(combinedCriteria);
                }
            }
        }
        criteriaQuery.where(criteriaList.get(0));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Business> findBusinesses(String query) {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Business.class);
        businessRoot = criteriaQuery.from(Business.class);
        ArrayList<String> terms = this.searchQueryKeywords(query);
        if (terms.size() > 0) {
            List<Business> businesses = this.queryProcess(terms, false);
            List<Business> partialBusinesses = this.queryProcess(terms, true);
            partialBusinesses.removeAll(businesses);
            businesses.addAll(partialBusinesses);
            return businesses;
        } else {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

}
