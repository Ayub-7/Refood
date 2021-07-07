package org.seng302.finders;

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

    /**
     * Searches for users by exact name, quoted names, then like names. Search results are case insensitive.
     * @param query string query containing the names requesting to be searched.
     * @return A list of users matching the query.
     */
    public List<User> queryByName(String query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        // Split query into different terms, and group quoted ("") params into a single term.
        ArrayList<String> terms = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(query);
        while (matcher.find()) {
            terms.add(matcher.group().replace("\"", ""));
        }

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

                if (criteriaList.size() == 0) {
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
        List<User> users = entityManager.createQuery(criteriaQuery).getResultList();

        // LIKE TERMS:
        logic = Logic.NONE;
        consecutive = 0;
        List<Predicate> likeCriterias = new ArrayList<>();

        for (String term : terms) {
            if (term.split(" ").length == 1) {

                if (term.strip().equals("AND")) {
                    logic = Logic.AND;
                    consecutive = 0;
                }
                else if (term.strip().equals("OR")) {
                    logic = Logic.OR;
                    consecutive = 0;
                } else {
                    if (consecutive == 1) {
                        consecutive = 0;
                        logic = Logic.AND;
                    } else {
                        consecutive++;
                    }

                    term = term.strip().toLowerCase();
                    Predicate p1 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + term + "%");
                    Predicate p2 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + term + "%");
                    Predicate p3 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("middleName")), "%" + term + "%");
                    Predicate p4 = criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("nickname")), "%" + term + "%");
                    Predicate finalCriteria = criteriaBuilder.or(p1, p2, p3, p4);

                    if (likeCriterias.size() == 0) {
                        logic = logic.NONE;
                        likeCriterias.add(finalCriteria);
                    }
                    if (logic == Logic.AND) {
                        Predicate predicate = criteriaBuilder.and(likeCriterias.get(0), finalCriteria);
                        likeCriterias.remove(0);
                        likeCriterias.add(predicate);
                        logic = Logic.NONE;

                    }
                    else if (logic == Logic.OR) {
                        Predicate predicate = criteriaBuilder.or(likeCriterias.get(0), finalCriteria);
                        likeCriterias.remove(0);
                        likeCriterias.add(predicate);
                        logic = Logic.NONE;
                    }
                    else {
                        likeCriterias.add(finalCriteria);
                    }
                }
            }
        }

        criteriaQuery.where(likeCriterias.get(0));
        List<User> partialUsers = entityManager.createQuery(criteriaQuery).getResultList();

        // Remove any duplicates and add the users obtained by LIKE terms to users list.
        partialUsers.removeAll(users);
        users.addAll(partialUsers);

        return users;
    }
}
