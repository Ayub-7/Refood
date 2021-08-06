package org.seng302.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Entity that represents a single like between a user and a business sale listing.
 * Key is a composite key comprised of the user id and listing id.
 * @see ListingLikeId
 */
@Data
@Entity
@IdClass(ListingLikeId.class)
public class ListingLike {

    @Id
    private long userId;

    @Id
    private long listingId;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "listing_id", insertable = false, updatable = false)
    private Listing listing;

}
