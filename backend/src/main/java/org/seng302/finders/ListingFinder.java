package org.seng302.finders;

import org.seng302.models.BusinessType;
import org.seng302.models.Listing;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ListingFinder {

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
     * Query criteria for business/seller name
     * @param term part of query that helps find the desired business(es)
     * @return Specification object that contains Listings.
     */
    private Specification<Listing> sellerContains(String term) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("product")
                .get("business").get("name")), "%"+term.toLowerCase()+"%");
    }

    private Specification<Listing> businessTypeSpec(String term) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("inventoryItem").get("product")
                .get("business").get("businessType"), BusinessType.valueOf(term.toUpperCase().replace(' ', '_').replace('-', '_')));
    }

    /**
     * Helper function that checks the fields if a predicate contains AND, OR, or any other term.
     * @param currentSpecification Specification object to be checked
     * @param nextTerm The upcoming term in the query
     * @param predicate Predicate object
     * @return Specification<Listing> object
     */
    private Specification<Listing> checkFields(Specification currentSpecification, String queryBy, String nextTerm, Logic predicate) {
        Specification<Listing> newSpec;
        if (queryBy.equalsIgnoreCase("seller")) {
            newSpec = sellerContains(nextTerm);
        } else {
            newSpec = businessTypeSpec(nextTerm);
        }
        if (predicate.equals(Logic.AND)) {
            currentSpecification = currentSpecification.and(newSpec);
        } else if (predicate.equals(Logic.OR)) {
            currentSpecification = currentSpecification.or(newSpec);
        }
        return currentSpecification;
    }

    /**
     * Gets next specification will perform AND or OR operation depending on current term and next term
     * @param specification current specification
     * @param term current term
     * @param terms list of terms, used to get next term in sequence
     * @return Specification<Listing> current specification with AND or OR operation applied
     */
    private Specification<Listing> getNextSpecification(Specification<Listing> specification, String queryBy, String term, ArrayList<String> terms) {
        if (terms.indexOf(term) != terms.size() - 1) {
            String nextTerm = terms.get(terms.indexOf(term) + 1).trim();
            if (term.strip().equals("AND")) {
                specification = checkFields(specification, queryBy, nextTerm, Logic.AND);
            } else if (term.strip().equals("OR")) {
                specification = checkFields(specification, queryBy, nextTerm, Logic.OR);
            } else if(!nextTerm.equals("AND") && !nextTerm.equals("OR")) {
                specification = checkFields(specification, queryBy, nextTerm, Logic.AND);
            }
        }
        return specification;

    }

    /**
     * Builds full specification object to be used in repository query
     * @param query search query, will be split up into terms and processed
     * @return Specification<Listing> resulting specification that will contain all predicates
     */
    private Specification<Listing> buildAddressSpec(String query, String queryBy) {
        ArrayList<String> terms = searchQueryKeywords(query);
        System.out.println(terms);
        Specification<Listing> specification;
        if (query.equalsIgnoreCase("seller")) {
            specification = sellerContains(terms.get(0));
        } else {
            specification = businessTypeSpec(terms.get(0));
        }
        for (String term : terms) {
            specification = getNextSpecification(specification, queryBy, term, terms);
        }
        return specification;
    }

    /**
     * The only publicly available method to access outside of this class to search for listings
     * @param query The search query to be used to filter search results
     * @return Will return all products if query is blank, otherwise will filter according to what is in the query
     */
    public Specification<Listing> findListing(String query, String queryBy) {
        Specification<Listing> matches = buildAddressSpec(query, queryBy);
        return matches;
    }
}
