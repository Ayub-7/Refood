package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CardStatus Enumerator
 */

public enum NotificationStatus {
    EXPIRED("Expired"),
    BOUGHT("Bought"),
    DELETED("Deleted");

    private final String label;

    NotificationStatus(String label) {
        this.label = label;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.label;
    }
}
