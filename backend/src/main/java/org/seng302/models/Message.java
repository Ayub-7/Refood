package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class for a user message.
 * Represents a single message sent from one user, to another user, regarding a community card.
 */
@Data
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User sender;

    @OneToOne
    private Card card;

    @OneToOne
    private User receiver;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+12")
    private Date sent;

    /**
     * Constructor for a new message object
     * @param sender User that sends the message
     * @param receiver User that receives the messages
     * @param card Card the message is sent from
     * @param description Message's description
     * @param sent Date the message was sent
     */
    public Message(User sender, User receiver, Card card, String description, Date sent) {
        this.sender = sender;
        this.receiver = receiver;
        this.card = card;
        this.description = description;
        this.sent = sent;
    }

    /**
     * Empty constructor for JPA use.
     */
    protected Message() {
    }
}
