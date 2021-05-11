package org.seng302.models.requests;

import lombok.Data;
import java.util.Date;

/**
 * DTO class that holds inventory info when added a product to it.
 */
@Data
public class NewInventoryRequest {

    private int quantity;
    private double pricePerItem;
    private double totalPrice;
    private Date manufacturedDate;
    private Date sellByDate;
    private Date bestBeforeDate;
    private Date expiryDate;


    public NewInventoryRequest(int quantity, double pricePerItem, double totalPrice, Date manufacturedDate, Date sellByDate, Date bestBeforeDate, Date expiryDate) {
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
        this.manufacturedDate = manufacturedDate;
        this.sellByDate = sellByDate;
        this.bestBeforeDate = bestBeforeDate;
        this.manufacturedDate = manufacturedDate;
        this.expiryDate = expiryDate;
    }
}
