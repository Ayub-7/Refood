package org.seng302.finders;

import javax.persistence.criteria.Join;
import org.seng302.models.Product;
import org.seng302.models.Business;
import org.seng302.models.Listing;
import org.seng302.models.Address;
import org.seng302.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.seng302.finders.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AddressFinder {

    @Autowired
    BusinessRepository businessRepository;


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
    private Specification<Listing> countryContains(String term) {
        return (root, query, criteriaBuilder)
        -> criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("product").get("business").get("address").get("country")), "%"+term.toLowerCase()+"%");
    }

    private Specification<Listing> suburbContains(String term) {
      return (root, query, criteriaBuilder)
            -> criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("product").get("business").get("address").get("suburb")), "%"+term.toLowerCase()+"%");
    }

    private Specification<Listing> cityContains(String term) {
      return (root, query, criteriaBuilder)
            -> criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("product").get("business").get("address").get("city")), "%"+term.toLowerCase()+"%");
    }

    private Specification<Listing> regionContains(String term) {
      return (root, query, criteriaBuilder)
            -> criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryItem").get("product").get("business").get("address").get("region")), "%"+term.toLowerCase()+"%");
    }


    private Specification<Listing> buildQuery(String term) {
        Specification<Listing> newSpec = countryContains(term);
        newSpec = newSpec.or(suburbContains(term));
        newSpec = newSpec.or(cityContains(term));
        newSpec = newSpec.or(regionContains(term));

        return newSpec;
    }

    private Specification<Listing> checkFields(Specification currentSpecification, String nextTerm, Logic predicate) {
        Specification<Listing> newSpec = buildQuery(nextTerm);

        if(predicate.equals(Logic.AND)) {
            currentSpecification = currentSpecification.and(newSpec);
        } else if (predicate.equals(Logic.OR)) {
             currentSpecification = currentSpecification.or(newSpec);
        }
        return currentSpecification;
    }


    /**
     * Builds full specification object to be used in repository query
     * @param query search query, will be split up into terms and processed
     * @return Specification<Product> resulting specification that will contain all predicates
     */
    private Specification<Listing> buildAddressSpec(String query) {
        ArrayList<String> terms = searchQueryKeywords(query);
        System.out.println(terms);
        Specification<Listing> specification = buildQuery(terms.get(0));
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
    private Specification<Listing> getNextSpecification(Specification<Listing> specification, String term, ArrayList<String> terms) {
        if (terms.indexOf(term) != terms.size() - 1) {
            String nextTerm = terms.get(terms.indexOf(term) + 1).trim();

            if (term.strip().equals("AND")) {
                specification = checkFields(specification, nextTerm, Logic.AND);
            } else if (term.strip().equals("OR")) {
                specification = checkFields(specification, nextTerm, Logic.OR);
            } else if(!nextTerm.equals("AND") && !nextTerm.equals("OR")) {
                specification = checkFields(specification, nextTerm, Logic.AND);
            }
        }
        return specification;
    }

    /**
     * The only publicly available method to access outside of this class to search for products
     * @param query The search query to be used to filter search results
     * @return Will return all products if query is blank, otherwise will filter according to what is in the query
     */
    public Specification<Listing> findAddress(String query) {
        Specification<Listing> matches = buildAddressSpec(query);
        return matches;
    }
}
