package org.seng302.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Entity that represents a single like between a user and a business sale listing.
 * Key is a composite key comprised of the user id and listing id.
 * @see LikeId
 */
@Data
@Entity
@IdClass(LikeId.class)
public class Like {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @OneToOne
    @JoinColumn(name = "listing_id", referencedColumnName = "id")
    private Listing listing;

}
