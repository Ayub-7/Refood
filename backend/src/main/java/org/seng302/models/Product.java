package org.seng302.models;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Data
@Entity
@IdClass(ProductId.class)
public class Product {

    @Id
    private String id;
    @JsonIgnore
    @Id
    private long businessId;

    private String name;
    private String description;
    private double recommendedRetailPrice;
    private Date created;
    // images when we get to it.


    public Product(String id, long businessId, String name, String description, double recommendedRetailPrice) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.description = description;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = new Date();
    }

    public Product() {

    }
}
