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

    protected Product() { }

}
