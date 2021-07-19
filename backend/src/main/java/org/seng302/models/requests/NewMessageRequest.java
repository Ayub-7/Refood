package org.seng302.models.requests;

import org.seng302.models.*;

import lombok.Data;

/**
 * Simple DTO class that contains the info of a new message.
 */
@Data
public class NewMessageRequest {
    private Card card;
    private String description;

    public NewMessageRequest(Card card, String description) {
        this.card = card;
        this.description = description;
    }
}
