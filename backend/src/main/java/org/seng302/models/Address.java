package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

/**
 * Entity class that holds information of an address for a user or business.
 */
@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    @NotNull
    private String country;
    private String postcode;

    /**
     * Constructor for Spring JPA use.
     */
    protected Address() {}

    public Address(String streetNumber, String streetName, String city, String region, String country, String postcode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.region = region;
        this.country = country;
        this.postcode = postcode;
    }
}
