package org.seng302.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Id class that represents the composite key of a listing like.
 */
@Embeddable
public class LikeId implements Serializable {

    private long userId;
    private long listingId;

}
