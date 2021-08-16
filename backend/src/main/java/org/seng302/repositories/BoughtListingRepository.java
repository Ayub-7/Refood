package org.seng302.repositories;


import org.seng302.models.BoughtListing;
import org.seng302.models.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * JPA Repository for the Listing entity/table.
 */
@RepositoryRestResource
public interface BoughtListingRepository extends JpaRepository<BoughtListing, Long> {

}
