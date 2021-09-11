package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MutedStatus {
    UNMUTED("Unmuted"),
    MUTED("Muted");

    private final String label;

    MutedStatus(String label) { this.label = label; }

    @Override
    @JsonValue
    public String toString() { return this.label; }
}
