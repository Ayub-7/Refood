package org.seng302.models.requests;

import lombok.Data;

@Data
public class NewProductRequest {

    private String id;
    private String name;
    private String description;
    private Double recommendedRetailPrice;

    public NewProductRequest(String id, String name, String description, Double recommendedRetailPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recommendedRetailPrice = recommendedRetailPrice;
    }
}
