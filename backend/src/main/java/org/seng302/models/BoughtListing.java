package org.seng302.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class BoughtListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private Product product;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date sold;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date listed;

    private int likes;

    private int quantity;

    /**
     * Empty constructor for JPA use
     */
    protected BoughtListing() {}

    /**
     * Bought Listing constructor class, created when a listing is sold.
     * @param buyer
     * @param product
     * @param likes
     * @param quantity
     */
    public BoughtListing(User buyer, Product product, int likes, int quantity, Date listed) {
        this.buyer = buyer;
        this.product = product;
        this.likes = likes;
        this.quantity = quantity;
        this.sold = new Date();
        this.listed = listed;
    }
}
