package org.seng302.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "business_id")
    private long businessId;

    private boolean muted;

    /**
     * Empty constructor for JPA use
     */
    public WishlistItem() {}

    /**
     * Basic constructor to wishlist a new business given a user id and business id.
     * @param userId the entity that is following the business.
     * @param businessId the entity that is being followed by the user.
     */
    public WishlistItem(Long userId, Long businessId) {
        this.userId = userId;
        this.businessId = businessId;
        this.muted = false;
    }

    /**
     * Function to set wishlistItem to muted, so user will not receive notifications from the
     * followed business
     */
    public void muteBusiness() {
        this.muted = true;
    }

    /**
     * Function to unmute wishlistItem, so the user will receive notifications from the
     * followed business
     */
    public void unmuteBusiness() {
        this.muted = false;
    }
}
