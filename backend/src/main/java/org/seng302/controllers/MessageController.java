package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.*;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.models.responses.CardIdResponse;
import org.seng302.repositories.CardRepository;
import org.seng302.repositories.MessageRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Controller class that handles the endpoints of community marketplace cards.
 */
@RestController
public class MessageController {

    private static final Logger logger = LogManager.getLogger(CardController.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CardRepository cardRepository;


    /**
     * Gets user messages, does this by grabbing user from their id, then finding the cards that the user has, then finding the messages that are related to those cards
     * Preconditions: Logged in and acting as a user
     * Postconditions: User's messages will be retrieved if any exist
     * @param userId ID of user that we are going to get messages from
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<String> getUserMessages(@PathVariable long userId) throws JsonProcessingException {
        User user = userRepository.findUserById(userId);
        List<Card> userCards = cardRepository.findCardsByUser(user);
        List<Message> messages = messageRepository.findByCardIn(userCards);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(messages));
    }

}
