package org.seng302.finders;

import org.seng302.models.BusinessType;
import org.seng302.models.Listing;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BusinessTypeFinder {

    private ArrayList<String> searchQueryKeywords(String query) {
        ArrayList<String> terms = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\"]\\S*|\"[^\"]*+\")\\s*").matcher(query);
        while (matcher.find()) {
            terms.add(matcher.group().replace("\"", ""));
        }
        return terms;
    }

    private Specification<Listing> businessTypeSpec(String term) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("inventoryItem").get("product")
                .get("business").get("businessType"), BusinessType.valueOf(term.toUpperCase().replace(' ', '_').replace('-', '_')));
    }

    private Specification<Listing> checkFields(Specification currentSpecification, String nextTerm, Logic predicate) {
        Specification<Listing> newSpec = businessTypeSpec(nextTerm);
        if (predicate.equals(Logic.AND)) {
            currentSpecification = currentSpecification.and(newSpec);
        } else if (predicate.equals(Logic.OR)) {
            currentSpecification = currentSpecification.or(newSpec);
        }
        return currentSpecification;
    }

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

    private Specification<Listing> buildBusinessTypeSpec(String query) {
        ArrayList<String> terms = searchQueryKeywords(query);
        System.out.println(terms);
        Specification<Listing> specification;
        specification = businessTypeSpec(terms.get(0));
        for (String term : terms) {
            specification = getNextSpecification(specification, term, terms);
        }
        return specification;
    }

    public Specification<Listing> findListingByBizType(String query) {
        Specification<Listing> matches = buildBusinessTypeSpec(query);
        return matches;
    }
}
