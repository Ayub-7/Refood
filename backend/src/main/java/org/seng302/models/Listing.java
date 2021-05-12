package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

}
