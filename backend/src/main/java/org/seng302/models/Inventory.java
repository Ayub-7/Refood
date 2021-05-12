package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.seng302.models.requests.NewInventoryRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity class that holds the information of a businesss product inventory.
 */
@Data
@Entity
public class Inventory {

    // Composite key of product id & business id.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="product_id"),
            @JoinColumn(name="business_id")
    })
    private Product product;

    private int quantity;
    private double pricePerItem;
    private double totalPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date manufacturedDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sellByDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date bestBeforeDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;

    protected Inventory() { }

    public Inventory(long id, Product product, int quantity, double pricePerItem, double totalPrice, Date manufacturedDate, Date sellByDate, Date bestBeforeDate, Date expiryDate) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
        this.manufacturedDate = manufacturedDate;
        this.sellByDate = sellByDate;
        this.bestBeforeDate = this.bestBeforeDate;
        this.expiryDate = this.expiryDate;
    }

    /**
     * Used for when a new product request is called.
     * @param newInventoryRequest The request body information that was mapped into a NewInventoryRequest.
     * @param businessId business to assign the product inventory rights to.
     * @param product the product being added to the businesses inventory
     */
    public Inventory(NewInventoryRequest newInventoryRequest, Long businessId) {
        this.quantity = newInventoryRequest.getQuantity();
        this.pricePerItem = newInventoryRequest.getPricePerItem();
        this.totalPrice = newInventoryRequest.getTotalPrice();
        this.manufacturedDate = newInventoryRequest.getManufacturedDate();
        this.sellByDate = newInventoryRequest.getSellByDate();
        this.bestBeforeDate = newInventoryRequest.getBestBeforeDate();
        this.manufacturedDate = newInventoryRequest.getManufacturedDate();
        this.expiryDate = newInventoryRequest.getExpiryDate();
        //this.created = new Date();
    }

}
