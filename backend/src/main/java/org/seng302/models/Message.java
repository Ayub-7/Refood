package org.seng302.models;

import lombok.Data;
import org.seng302.models.requests.NewMessageRequest;

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

    /**
     * New Card request uses the minimum attributes and a reference to the User who created the card for initialization
     * This intializer converts a NewCardRequest to a Card entity
     * The date created is set to the date this constructor is called.
     * @param newCardRequest see NewCardRequest.java. Creates a new card using minimum fields
     * @param user the user object that is creating the new community card.
     */
    public Message(NewMessageRequest newMessageRequest, User user) throws ValidationException {
        try {
            if (validateNewCard(newCardRequest)) {
                this.sender = user;
                this.receiver = newMessageRequest.getReceiver();
                this.card = newMessageRequest.getCard();
                this.description = newMessageRequest.getDescription();
                this.sent = new Date();
            }
        }
        catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    /**
     * Validates a new Message object being created from a NewMessageRequest DTO.
     * @param newMessageRequest DTO class containing the info for a new Message entity
     * @return true if the Message information is valid.
     * @throws ValidationException if any of the Message information is invalid.
     */
    private boolean validateNewMessage(NewMessageRequest newMessageRequest) throws ValidationException {

        //no receiver
        if (newMessageRequest.getReceiver() == null) {
            throw new ValidationException("Receiver cannot be null");
        }

        //in case they send to an invalid user.
        User user = userRepository.findUserById(newMessageRequest.getReceiver().getId());

        if (user == null) {
            throw new ValidationException("Receiver does not exist");
        }

        //no card
        if (newMessageRequest.getCard() == null) {
            throw new ValidationException("Message must have an associated Card");
        }
        //todo: check in case the supplied card id is invalid

        //Blank or null description
        if (newMessageRequest.getDescription() == null || newMessageRequest.getDescription() == '') {
            throw new ValidationException("Message must have a description");
        }
        return true;
    }
}
