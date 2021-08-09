package org.seng302.finders;

import org.seng302.models.Business;
import org.seng302.models.BusinessType;
import org.seng302.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class BusinessFinder {

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
     * @return Specification<Business> containing matches for name
     */
    private Specification<Business> nameContains(String name) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+name.toLowerCase()+"%");
    }

    /**
     * Retrieve specification matches for all business with businessType matching the given type. Used to further filter
     * businesses for searching.
     * @param type Business type string
     * @return Specification<Business> with all businesses with
     */
    private Specification<Business> typeFilter(String type) throws ResponseStatusException {
        String attribute = "businessType";
        switch (type.toUpperCase()) {
            case "ACCOMMODATION AND FOOD SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(criteriaBuilder.lower(root.get(attribute)), BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
            case "RETAIL TRADE":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.RETAIL_TRADE);
            case "CHARITABLE ORGANISATION":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.CHARITABLE_ORGANISATION);
            case "NON-PROFIT ORGANISATION":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.NON_PROFIT_ORGANISATION);
            case "ADMINISTRATIVE AND SUPPORT SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.ADMINISTRATIVE_AND_SUPPORT_SERVICES);
            case "AGRICULTURE FORESTRY AND FISHING":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.AGRICULTURE_FORESTRY_AND_FISHING);
            case "ARTS AND RECREATION SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.ARTS_AND_RECREATION_SERVICES);
            case "CONSTRUCTION":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.CONSTRUCTION);
            case "EDUCATION AND TRAINING":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.EDUCATION_AND_TRAINING);
            case "ELECTRICITY GAS WATER AND WASTE SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.ELECTRICITY_GAS_WATER_AND_WASTE_SERVICES);
            case "FINANCIAL AND INSURANCE SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.FINANCIAL_AND_INSURANCE_SERVICES);
            case "HEALTH CAR AND SOCIAL ASSISTANCE":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.HEALTH_CAR_AND_SOCIAL_ASSISTANCE);
            case "INFORMATION MEDIA AND TELECOMMUNICATION":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.INFORMATION_MEDIA_AND_TELECOMMUNICATION);
            case "MANUFACTURING":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.MANUFACTURING);
            case "MINING":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.MINING);
            case "PROFESSIONAL SCIENTIFIC AND TECHNICAL SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.PROFESSIONAL_SCIENTIFIC_AND_TECHNICAL_SERVICES);
            case "PUBLIC ADMINISTRATION AND SAFETY":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.PUBLIC_ADMINISTRATION_AND_SAFETY);
            case "RENTAL HIRING AND REAL ESTATE SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.RENTAL_HIRING_AND_REAL_ESTATE_SERVICES);
            case "TRANSPORT POSTAL AND WAREHOUSING":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.TRANSPORT_POSTAL_AND_WAREHOUSING);
            case "WHOLESALE TRADE":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.WHOLESALE_TRADE);
            case "OTHER SERVICES":
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get(attribute), BusinessType.OTHER_SERVICES);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid businessType");
        }
    }


    /**
     * Builds full specification object to be used in repository query
     * @param query search query, will be split up into terms and processed
     * @return Specification<Product> resulting specification that will contain all predicates
     */
    private Specification<Business> buildBusinessSpec(String query, String type) throws ResponseStatusException {
        ArrayList<String> terms = searchQueryKeywords(query);
        Specification<Business> specification = nameContains(terms.get(0).trim());
        for (String term : terms) {
            specification = getNextSpecification(specification, term, terms);
        }
        if (type != null && type.length() > 0) {
            specification = specification.and(typeFilter(type));
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
    private Specification<Business> getNextSpecification(Specification<Business> specification, String term, ArrayList<String> terms) {
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
    public Specification<Business> findBusinesses(String query, String type) throws ResponseStatusException {
        return buildBusinessSpec(query, type);
    }
}




