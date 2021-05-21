package org.seng302.models.requests;

import java.util.Date;
import lombok.Data;

@Data
public class NewListingRequest {
    private long inventoryItemId;
    private int quantity;
    private double price;
    private String moreInfo;
    private Date closes;


    public NewListingRequest(long inventoryItemId, int quantity, double price, String moreInfo, Date closes) {
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
        this.price = price;
        this.moreInfo = moreInfo;
        this.closes = closes;
    }
}
