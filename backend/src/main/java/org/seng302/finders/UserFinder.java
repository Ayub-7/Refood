package org.seng302.finders;

import org.seng302.models.Business;
import org.seng302.models.User;
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

/**
 * Class containing methods to search for users with specific query params.
 */
@Component
public class UserFinder {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaQuery<User> criteriaQuery;
    private CriteriaBuilder criteriaBuilder;
    private Root<User> userRoot;


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
     * This method is used to build queries to filter Users based on firstname, middlename, lastname, and nickname.
     * @param term Subquery to filter with.
     * @param isLike If true, the subquery could be part of someone's name.
     *               Otherwise, the subquery has to exactly match the name.
     * @return Predicate that will be used to query users.
     */
    private Predicate criteriaBuilder(String term, boolean isLike) {
        if (!isLike) {
            String[] subTerms = term.split(" ");
            List<Predicate> subTermPredicates = new ArrayList<>();

            for (String subTerm : subTerms) {
                subTerm = subTerm.strip().toLowerCase();
                Predicate c1 = criteriaBuilder.equal(criteriaBuilder.lower(userRoot.get("firstName")), subTerm);
                Predicate c2 = criteriaBuilder.equal(criteriaBuilder.lower(userRoot.get("lastName")), subTerm);
                Predicate c3 = criteriaBuilder.equal(criteriaBuilder.lower(userRoot.get("middleName")), subTerm);
                Predicate c4 = criteriaBuilder.equal(criteriaBuilder.lower(userRoot.get("nickname")), subTerm);
                Predicate criteria = criteriaBuilder.or(c1, c2, c3, c4);
                subTermPredicates.add(criteria);
            }

            Predicate combinedCriteria = subTermPredicates.get(0);
            for (int i = 1; i < subTerms.length; i++) {
                combinedCriteria = criteriaBuilder.and(combinedCriteria, subTermPredicates.get(i));
            }

            return combinedCriteria;
        } else {
            term = term.strip().toLowerCase();
            Predicate p1 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + term + "%");
            Predicate p2 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + term + "%");
            Predicate p3 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("middleName")), "%" + term + "%");
            Predicate p4 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("nickname")), "%" + term + "%");
            return criteriaBuilder.or(p1, p2, p3, p4);
        }
    }


    /**
     * This method is used to query Users to match the query exactly or contain the query.
     * @param terms ArrayList of subqueries to be used for querying
     * @param isLike If true, will get all results that contain the query.
     *               Otherwise, will get results that exactly match the query
     * @return List of Users
     */
    private List<User> queryProcess(ArrayList<String> terms, boolean isLike) {
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


    /**
     * Searches for users by exact name, quoted names, then like names. Search results are case insensitive.
     * @param query string query containing the names requesting to be searched.
     * @return A list of users matching the query.
     */
    public List<User> queryByName(String query) {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(User.class);
        userRoot = criteriaQuery.from(User.class);
        ArrayList<String> terms = this.searchQueryKeywords(query);
        if (terms.size() == 0) {
            List<User> users = this.queryProcess(terms, false);
            List<User> partialUsers = this.queryProcess(terms, true);
            partialUsers.removeAll(users);
            users.addAll(partialUsers);
            return users;
        } else {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }
}
