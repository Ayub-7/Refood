package org.seng302.finders;


import org.seng302.models.Product;
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
public class ProductFinder {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaQuery<Product> criteriaQuery;
    private CriteriaBuilder criteriaBuilder;
    private Root<Product> productRoot;


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


    private Predicate criteriaBuilder(String term, boolean isLike) {
        if (!isLike) {
            String[] subTerms = term.split(" ");
            List<Predicate> subTermPredicates = new ArrayList<>();

            for (String subTerm : subTerms) {
                subTerm = subTerm.strip().toLowerCase();
                Predicate criteria = criteriaBuilder.equal(criteriaBuilder.lower(productRoot.get("name")), subTerm);
                subTermPredicates.add(criteria);
            }

            Predicate combinedCriteria = subTermPredicates.get(0);
            for (int i = 1; i < subTerms.length; i++) {
                combinedCriteria = criteriaBuilder.and(combinedCriteria, subTermPredicates.get(i));
            }

            return combinedCriteria;
        } else {
            term = term.strip().toLowerCase();
            Predicate name = criteriaBuilder.like(criteriaBuilder.lower(productRoot.get("name")), "%" + term + "%");

            return name;

        }
    }


    /**
     * This method is used twice in this class to filter the exact matching results and to filter similar results.
     * @param terms List of subqueries to be used for searching.
     * @param isLike If true, it will return results with things that contains query as a whole word or a part of a word.
     *               Otherwise, it will return results that exactly match the query
     * @return Return a list of businesses
     */
    private List<Product> queryProcess(ArrayList<String> terms, boolean isLike) {
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
     * The only publicly available method to access outside of this class to search for products
     * @param query The search query to be used to filter search results
     * @return Will return all businesses if query is blank, otherwise will filter according to what is in the query
     */
    public List<Product> findProduct(String query) {
        criteriaBuilder = entityManager.getCriteriaBuilder();

        criteriaQuery = criteriaBuilder.createQuery(Product.class);
        productRoot = criteriaQuery.from(Product.class);
        ArrayList<String> terms = this.searchQueryKeywords(query);
        if (terms.size() > 0) {
            List<Product> products = this.queryProcess(terms, false);
            List<Product> partialProducts = this.queryProcess(terms, true);
            partialProducts.removeAll(products);
            products.addAll(partialProducts);
            return products;
        } else {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }
}
