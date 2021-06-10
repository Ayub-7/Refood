package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seng302.models.Card;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.models.responses.CardIdResponse;
import org.seng302.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Controller class that handles the endpoints of community marketplace cards.
 */
@RestController
public class CardController {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CardRepository cardRepository;


    /**
     * POST/Creates a new card to store in the database that will go onto the community marketplace.
     * @param newCardRequest DTO that contains the request body of the new card being created.
     * @param session the current user session
     * @return 401 if not logged in (handled by spring sec), 403 if creatorId, session user Id do not match or if not a D/GAA,
     * 400 if there are errors with data, 201 otherwise.
     * @throws JsonProcessingException if mapper to convert the response into a JSON string fails.
     */
    @PostMapping("/cards")
    public ResponseEntity<String> addCard(@RequestBody NewCardRequest newCardRequest, HttpSession session) throws JsonProcessingException {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        // Checking card info is valid/has required fields.
        if (newCardRequest.getSection() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Marketplace section is missing.");
        }

        if (newCardRequest.getKeywords() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card keywords is missing.");
        }

        // Attempting to create a card for somebody else.
        if (newCardRequest.getCreatorId() != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Card newCard = new Card(newCardRequest, currentUser);
        cardRepository.save(newCard);

        CardIdResponse cardIdResponse = new CardIdResponse(newCard.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.writeValueAsString(cardIdResponse));
    }

}
