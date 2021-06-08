package org.seng302.repositories;

import org.seng302.models.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;
import org.seng302.models.*;

import java.util.List;

/**
 * JPA Repository for the Card entity/table.
 */
@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findCardsById(long id);

}
