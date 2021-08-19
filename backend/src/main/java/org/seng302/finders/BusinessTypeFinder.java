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

    private Specification<Listing> buildBusinessTypeSpec(String query) {
        ArrayList<String> terms = searchQueryKeywords(query);
        System.out.println(terms);
        Specification<Listing> specification;
        specification = businessTypeSpec(terms.get(0));
        return specification;
    }

    public Specification<Listing> findListingByBizType(String query) {
        return buildBusinessTypeSpec(query);
    }
}
