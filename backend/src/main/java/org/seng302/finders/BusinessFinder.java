package org.seng302.finders;

import org.seng302.models.Business;
import org.seng302.models.BusinessType;
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

    /**
     * Helper function to break down query into subqueries
     * @param query Query to be broken. (It cannot take empty strings)
     * @return ArrayList of subqueries
     */
    private ArrayList<String> searchQueryKeywords(String query) {
        ArrayList<String> terms = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\"]\\S*|\"[^\"]*+\")\\s*").matcher(query);
        while (matcher.find()) {
            terms.add(matcher.group().replace("\"", ""));
        }
        return terms;
    }

    /**
     * Builds criteria to help with querying businesses
     * @param term query used for filtering businesses
     * @param type Type of business
     * @param isLike If true, it will require that a business' name either matches the query or part of its name has the query in it.
     *               Otherwise, the query must exactly match the business name.
     * @return Predicate that will be used to query businesses
     */
    private Predicate criteriaBuilder(String term, String type, boolean isLike) {
        //Obtains criteria
        Predicate businessType = null;
        if (type != null) {
            if (type.length() > 0) {
                switch (type.toUpperCase()) {
                    case "ACCOMMODATION AND FOOD SERVICES":
                        businessType = criteriaBuilder.equal(businessRoot.get("businessType"), BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
                        break;
                    case "RETAIL TRADE":
                        businessType = criteriaBuilder.equal(businessRoot.get("businessType"), BusinessType.RETAIL_TRADE);
                        break;
                    case "CHARITABLE ORGANISATION":
                        businessType = criteriaBuilder.equal(businessRoot.get("businessType"), BusinessType.CHARITABLE_ORGANISATION);
                        break;
                    case "NON-PROFIT ORGANISATION":
                        businessType = criteriaBuilder.equal(businessRoot.get("businessType"), BusinessType.NON_PROFIT_ORGANISATION);
                        break;
                }
            }
        }

        if (term.length() == 0) {
            return businessType;
        }

        if (!isLike) {
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
            if (businessType != null) {
                return criteriaBuilder.and(combinedCriteria, businessType);
            }
            return combinedCriteria;
        } else {
            term = term.strip().toLowerCase();
            Predicate name = criteriaBuilder.like(criteriaBuilder.lower(businessRoot.get("name")), "%" + term + "%");
            if (businessType != null) {
                return criteriaBuilder.and(name, businessType);
            }
            return name;
        }
    }

    /**
     * This method is used twice in this class to filter the exact matching results and to filter similar results.
     * @param terms List of subqueries to be used for searching.
     * @param type Type of business
     * @param isLike If true, it will return results with things that contains query as a whole word or a part of a word.
     *               Otherwise, it will return results that exactly match the query
     * @return Return a list of businesses
     */
    private List<Business> queryProcess(ArrayList<String> terms, String type, boolean isLike) {
        List<Predicate> criteriaList = new ArrayList<>();
        if (terms.isEmpty()) {
            criteriaList.add(this.criteriaBuilder("", type, isLike));
            criteriaQuery.where(criteriaList.get(0));
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
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

                Predicate combinedCriteria = this.criteriaBuilder(term, type, isLike);

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

    /**
     * The only publicly available method to access outside of this class to search for businesses.
     * @param query The search query to be used to filter search results
     * @param type Type of business
     * @return Will return all businesses if query is blank, otherwise will filter according to what is in the query
     */
    public List<Business> findBusinesses(String query, String type) {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Business.class);
        businessRoot = criteriaQuery.from(Business.class);
        ArrayList<String> terms = this.searchQueryKeywords(query);
        if (terms.size() > 0) {
            List<Business> businesses = this.queryProcess(terms, type, false);
            List<Business> partialBusinesses = this.queryProcess(terms, type, true);
            partialBusinesses.removeAll(businesses);
            businesses.addAll(partialBusinesses);
            return businesses;
        } else if (type.length() > 0) {
            List<Business> businesses = this.queryProcess(terms, type, false);
            return businesses;
        } else {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

}