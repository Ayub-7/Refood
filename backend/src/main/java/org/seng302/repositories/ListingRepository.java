package org.seng302.repositories;

import org.seng302.models.Listing;
import org.seng302.models.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JPA Repository for the Listing entity/table.
 */
@RepositoryRestResource
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findListingsByInventoryItem(Inventory inventoryItem);
}
