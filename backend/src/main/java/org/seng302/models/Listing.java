package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class that represents a listing for the sale of some product from a business' inventory.
 */
@Data
@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Inventory inventoryItem;
    private int quantity;
    private double price;
    private String moreInfo;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date closes;

    /**
     * Constructor a new Listing object. Id not included.
     * @param inventoryItem item that this Listing object will represent.
     * @param quantity the number of items associated with this Listing.
     * @param price the value of the Listing.
     * @param moreInfo an (optional) field for businesses to provide further info about the listing.
     * @param created date and t ime when the listing was created.
     * @param closes date and time when the listing closes.
     */
    public Listing(Inventory inventoryItem, int quantity, double price, String moreInfo, Date created, Date closes) {
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
        this.price = price;
        this.moreInfo = moreInfo;
        this.created = created;
        this.closes = closes;
    }

    /**
     * Empty constructor for JPA use.
     */
    protected Listing() {}
}
