package org.seng302.finders;


import org.seng302.models.Product;
import org.seng302.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductFinder {

    @Autowired
    ProductRepository productRepository;


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
     * Gets specification objects if their name matches query
     * @param name name of product, used to find all instances of
     * @return Specification<Product> containing matches for name
     */
    private Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder)
        -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+name.toLowerCase()+"%");
    }


    /**
     * Builds full specification object to be used in repository query
     * @param query search query, will be split up into terms and processed
     * @return Specification<Product> resulting specification that will contain all predicates
     */
    private Specification<Product> buildProductSpec(String query) {
        ArrayList<String> terms = searchQueryKeywords(query);
        Specification<Product> specification = nameContains(terms.get(0).trim());
        for (String term : terms) {
            specification = getNextSpecification(specification, term, terms);
        }

        return specification;
    }

    /**
     * Gets next specification will perform AND or OR operation depending on current term and next term
     * @param specification current specification
     * @param term current term
     * @param terms list of terms, used to get next term in sequence
     * @return Specification<Product> current specification with AND or OR operation applied
     */
    private Specification<Product> getNextSpecification(Specification<Product> specification, String term, ArrayList<String> terms) {
        if (terms.indexOf(term) != terms.size() - 1) {
            String nextTerm = terms.get(terms.indexOf(term) + 1).trim();

            if (term.strip().equals("AND")) {
                specification = specification.and(nameContains(nextTerm));
            } else if (term.strip().equals("OR")) {
                specification = specification.or(nameContains(nextTerm));
            } else if(!nextTerm.equals("AND") && !nextTerm.equals("OR")) {
                specification = specification.and(nameContains(nextTerm));
            }
        }

        return specification;

    }

    /**
     * The only publicly available method to access outside of this class to search for products
     * @param query The search query to be used to filter search results
     * @return Will return all products if query is blank, otherwise will filter according to what is in the query
     */
    public List<Product> findProduct(String query) {

        Specification<Product> matches = buildProductSpec(query);

        return productRepository.findAll(matches);


    }
}
