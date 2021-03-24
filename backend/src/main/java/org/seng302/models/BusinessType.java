package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum for business type. String representation can be obtained by the overridden toString() method
 */
public enum BusinessType {
    ACCOMMODATION_AND_FOOD_SERVICES("Accommodation and Food Services"),
    RETAIL_TRADE("Retail Trade"),
    CHARITABLE_ORGANISATION("Charitable organisation"),
    NON_PROFIT_ORGANISATION("Non-profit organisation");

    private final String label;

    BusinessType (String label) {
        this.label = label;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.label;
    }
}
