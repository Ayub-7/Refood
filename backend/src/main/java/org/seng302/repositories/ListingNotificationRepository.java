package org.seng302.repositories;

import org.seng302.models.ListingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface ListingNotificationRepository extends JpaRepository<ListingNotification, Long> {
}
