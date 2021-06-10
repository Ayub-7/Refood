package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.seng302.models.requests.NewCardRequest;

import javax.xml.bind.ValidationException;
import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;
import org.seng302.models.*;
import java.util.List;
import org.seng302.models.Address;

import java.util.Calendar;

/**
 * Entity class for a user created card
 *
 * Rather than include the user object itself, I have chosen only take their user name and address.
 * Possible information leak as when we send the card list to the front end, we sent the json object in it's entirety.
 * ie when including the user, it will also include every other field about them
 * we want only reveal the information required rather than expose a possibe security risk.
 */
@Data
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    private Date displayPeriodEnd;
    private String keywords;

    @JsonIgnore
    @Column(name = "section")
    private MarketplaceSection section;

    /**
     * Constructor for a new card object
     * @param user User that created the card
     * @param title Card's title
     * @param description Card's dectiption field
     * @param created Date the card was created
     * @param displayPeriodEnd Date the card will be removed
     * @param keywords Hashtags that describe the card
     * @param section The Card's Marketplace section (see the Enum, MarketplaceSection.java)
     */
    public Card(User user, String title, String description, Date created, Date displayPeriodEnd, String keywords, MarketplaceSection section ) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.created = created;
        this.displayPeriodEnd = displayPeriodEnd;
        this.keywords = keywords;
        this.section = section;
    }


    /**
     * Empty constructor for JPA use.
     */
    protected Card() {
    }

    /**
     * New Card request uses the minimum attributes and a reference to the User who created the card for initialization
     * This intializer converts a NewCardRequest to a Card entity
     * The date created is set to the date this constructor is called.
     * @param newCardRequest see NewCardRequest.java. Creates a new card using minimum fields
     * @param user the user object that is creating the new community card.
     */
    public Card(NewCardRequest newCardRequest, User user) {
        this.user = user;
        this.title = newCardRequest.getTitle();
        this.description = newCardRequest.getDescription();
        this.created = new Date();
        this.displayPeriodEnd = getDisplayPeriodEndDate();
        this.keywords = newCardRequest.getKeywords();
        this.section = newCardRequest.getSection();
    }

    /**
     * getDisplayPeriodEndDate calculates the time a Card's display period will end
     * This is set to two weeks/14 days.
     * To change, alter the const displayPeriod in the function.
     *
     * Since this is called when a new card is created, the displayPeriodEndDate is
     * displayPeriod days after the current date.
     *
     * @return displayPeriodEndDate The date the card will expire
     */
    private Date getDisplayPeriodEndDate () {
        int displayPeriod = 14;

        Calendar displayPeriodEndCalendar = Calendar.getInstance();
        displayPeriodEndCalendar.add(Calendar.DAY_OF_YEAR, displayPeriod);
        Date displayPeriodEndDate = displayPeriodEndCalendar.getTime();

        return displayPeriodEndDate;
    }


}
