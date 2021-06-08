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
    private String userName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address userHomeAddress; //only city and suburb...

    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    private String keywords; //comma delimited? Or use a List?

    @JsonIgnore
    @Column(name = "card_type")
    private MarketplaceSection section; //ForSale, wanted, exchange

    /**
     * Constructor for a new card object
     * @param userName
     * @param userHomeAddress
     * @param title
     * @param description
     * @param created
     * @param keywords
     * @param section
     */
    public Card(String userName, Address userHomeAddress, String title, String description, Date created, String keywords, MarketplaceSection section ) {
        this.userName = userName;
        this.userHomeAddress = userHomeAddress;
        this.title = title;
        this.description = description;
        this.created = created;
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
     * It creates a userName based on the user's first and last names
     * It creates a generalized address user the user's real address
     *
     * @param newCardRequest
     */
    public Card(NewCardRequest newCardRequest) {
        User user = newCardRequest.getUser();
        Address userAddress = user.getHomeAddress();

        Address generalAddress = new Address(null, null,
                userAddress.getSuburb(),
                userAddress.getCity(),
                userAddress.getRegion(),
                userAddress.getCountry(),
                userAddress.getPostcode());

        this.userName = user.getFirstName() + user.getLastName();
        this.userHomeAddress = generalAddress;
        this.title = newCardRequest.getTitle();
        this.description = newCardRequest.getDescription();
        this.created = new Date();
        this.keywords = newCardRequest.getKeywords();
        this.section = newCardRequest.getSection();
    }


}
