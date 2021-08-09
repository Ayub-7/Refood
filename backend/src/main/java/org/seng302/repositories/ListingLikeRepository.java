package org.seng302.repositories;

import org.seng302.models.ListingLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingLikeRepository extends JpaRepository<ListingLike, Long> {

    List<ListingLike> findListingLikesByUserId(long id);
}
