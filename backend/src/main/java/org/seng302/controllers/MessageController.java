package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.*;
import org.seng302.repositories.MessageRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller class that handles the endpoints of community marketplace cards.
 */
@RestController
public class MessageController {

    private static final Logger logger = LogManager.getLogger(MessageController.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    /**
     * Gets user messages, does this by grabbing user from their id, then finding the cards that the user has, then finding the messages that are related to those cards
     * Preconditions: Logged in and acting as a user
     * Postconditions: User's messages will be retrieved if any exist
     * @param userId ID of user that we are going to get messages from
     * @return 200 if successful, 40
     * @throws JsonProcessingException
     */
    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<String> getUserMessages(@PathVariable long userId, HttpSession session) throws JsonProcessingException {
        User user = userRepository.findUserById(userId);
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        if(userId != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Message> messages = messageRepository.findMessageByReceiver(user);
        for(Message message: messages) {
            message.getSender().setBusinessesAdministered(null);
            message.getReceiver().setBusinessesAdministered(null);
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(messages));
    }

}
