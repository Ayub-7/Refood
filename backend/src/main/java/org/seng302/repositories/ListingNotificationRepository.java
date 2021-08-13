package org.seng302.repositories;

import org.seng302.models.Listing;
import org.seng302.models.ListingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
public interface ListingNotificationRepository extends JpaRepository<ListingNotification, Long> {
    ListingNotification findListingNotificationsByUserIdAndListing(long id, Listing listing);
}

