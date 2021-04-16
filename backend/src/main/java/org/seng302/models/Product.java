package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.seng302.models.requests.NewProductRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity class that holds the information of a business product.
 */
@Data
@Entity
@IdClass(ProductId.class)
public class Product {

    // Composite key of product id & business id.
    @Id
    private String id;
    @Id
    @JsonIgnore
    private long businessId;

    private String name;
    private String description;
    private double recommendedRetailPrice;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;

    @OneToMany(cascade = CascadeType.ALL) // Creates a table PRODUCT_IMAGES.
    private List<Image> images;

    protected Product() { }

    public Product(String id, long businessId, String name, String description, double recommendedRetailPrice, Date created) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.description = description;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created;
        this.images = new ArrayList<>();
    }

    /**
     * Used for when a new product request is called.
     * @param newProductRequest The request body information that was mapped into a NewProductRequest.
     * @param businessId business to assign the product rights to.
     */
    public Product(NewProductRequest newProductRequest, Long businessId) {
        this.id = newProductRequest.getId();
        this.businessId = businessId;
        this.name = newProductRequest.getName();
        this.description = newProductRequest.getDescription();
        this.recommendedRetailPrice = newProductRequest.getRecommendedRetailPrice();
        this.created = new Date();
        this.images = new ArrayList<>();
    }

    /**
     * Adds a new image to the product entity.
     * @param image the image object to add.
     */
    public void addProductImage(Image image) {
        this.images.add(image);
    }

}
