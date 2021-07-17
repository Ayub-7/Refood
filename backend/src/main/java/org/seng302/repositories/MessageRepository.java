package org.seng302.repositories;

import org.seng302.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.List;

@RepositoryRestResource
public interface MessageRepository extends JpaRepository<Message, Long> {

    // I'm not actually sure if this works.
    List<Message> getAllByCardUserId(long id);

    /**
     * Get Message by it's ID
     *
     * @param id message's ID
     * @return Message
     */
    Message findMessageById(long id);

    /**
     * Delete a Message by it's ID
     *
     * @param id Message's ID
     * @return long number of records deleted
     */
    @Transactional
    long deleteMessageById(long id);

}
