package org.seng302.repositories;

import org.seng302.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface BusinessRepository extends JpaRepository<Business, Long> {

    /**
     * Finds a Business by their unique id
     * @param id unique id being queried
     * @return A Business object with the matching id if it exists
     */
    Business findBusinessById(long id);

}
