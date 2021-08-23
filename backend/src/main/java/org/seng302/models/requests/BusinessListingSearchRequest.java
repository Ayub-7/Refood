package org.seng302.models.requests;

import lombok.Data;
import org.seng302.models.BusinessType;

import java.util.Date;
import java.util.List;

/**
 * DTO request class that holds the information of a query to obtain listings of all businesses.
 */
@Data
public class BusinessListingSearchRequest {

    private String businessQuery;
    private String productQuery;
    private String addressQuery;

    private String sortBy;
    private List<BusinessType> businessTypes;
    private Double minPrice;
    private Double maxPrice;

    private Date minClosingDate;
    private Date maxClosingDate;

}
