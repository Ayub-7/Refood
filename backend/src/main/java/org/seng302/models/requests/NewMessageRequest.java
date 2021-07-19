package org.seng302.models.requests;

import org.seng302.models.*;

import lombok.Data;

/**
 * Simple DTO class that contains the info of a new message.
 */
@Data
public class NewMessageRequest {
    private User sender;
    private User receiver;
    private Card card;
    private String description;

    public NewMessageRequest(User sender, User receiver, Card card, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.card = card;
        this.description = description;
    }
}
