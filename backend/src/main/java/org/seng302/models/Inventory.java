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
        @JoinColumn(name="product_id", updatable = false, insertable = false),
        @JoinColumn(name="business_id", updatable = false, insertable = false)
    })
    private Product product;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "business_id")
    private long businessId;

    private int quantity;
    private double pricePerItem;
    private double totalPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date manufactured;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sellBy;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date bestBefore;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expires;

    protected Inventory() { }

    public Inventory(String productId, long businessId, int quantity, double pricePerItem, double totalPrice, Date manufacturedDate, Date sellByDate, Date bestBeforeDate, Date expiryDate) {
        this.productId = this.productId;
        this.businessId = this.businessId;
        this.quantity = this.quantity;
        this.pricePerItem = this.pricePerItem;
        this.totalPrice = this.totalPrice;
        this.manufactured = this.manufactured;
        this.sellBy = this.sellBy;
        this.bestBefore = this.bestBefore;
        this.expires = this.expires;
    }

    /**
     * Used for when a new product request is called.
     * @param newInventoryRequest The request body information that was mapped into a NewInventoryRequest.
     * @param businessId business to assign the product inventory rights to.
     * @param product the product being added to the businesses inventory
     */
    public Inventory(NewInventoryRequest newInventoryRequest, Long businessId) {
        this.productId = newInventoryRequest.getProductId();
        this.businessId = this.businessId;
        this.quantity = newInventoryRequest.getQuantity();
        this.pricePerItem = newInventoryRequest.getPricePerItem();
        this.totalPrice = newInventoryRequest.getTotalPrice();
        this.manufactured = newInventoryRequest.getManufactured();
        this.sellBy = newInventoryRequest.getSellBy();
        this.bestBefore = newInventoryRequest.getBestBefore();
        this.manufactured = newInventoryRequest.getManufactured();
        this.expires = newInventoryRequest.getExpires();
        //this.created = new Date();
    }

}
