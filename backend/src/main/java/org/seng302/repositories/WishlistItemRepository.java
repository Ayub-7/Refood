package org.seng302.repositories;

import org.seng302.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {


}
