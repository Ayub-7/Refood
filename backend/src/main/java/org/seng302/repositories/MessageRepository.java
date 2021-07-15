package org.seng302.repositories;

import org.seng302.models.Card;
import org.seng302.models.Message;
import org.seng302.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MessageRepository extends JpaRepository<Message, Long> {

    // I'm not actually sure if this works.
    List<Message> getAllByCard_User_Id(long id);

    List<Message> findByCardIn(List<Card> cards);

}
