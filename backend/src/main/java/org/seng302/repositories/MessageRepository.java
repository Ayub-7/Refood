package org.seng302.repositories;

import org.seng302.models.Message;
import org.seng302.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessageByReceiver(User receiver);

}
