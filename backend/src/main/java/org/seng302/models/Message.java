package org.seng302.models;

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

    private Date sent;

}
