package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.Card;
import org.seng302.models.MarketplaceSection;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.models.responses.CardIdResponse;
import org.seng302.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Controller class that handles the endpoints of community marketplace cards.
 */
@RestController
public class CardController {

    private static final Logger logger = LogManager.getLogger(CardController.class.getName());
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

        // Attempting to create a card for somebody else.
        if (newCardRequest.getCreatorId() != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Card newCard;
        try { // Attempt to create a new card.
            newCard = new Card(newCardRequest, currentUser);
        }
        catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        cardRepository.save(newCard);
        CardIdResponse cardIdResponse = new CardIdResponse(newCard.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.writeValueAsString(cardIdResponse));
    }

    /**
     * Retrieves all cards from a given section parameter.
     * @param section the string section name.
     * @return 401 (if no auth), 400 if section does not exist, 200 otherwise, along with the list of cards in that section.
     * @throws JsonProcessingException if converting the list of cards into a JSON string fails.
     */
    @GetMapping("/cards")
    public ResponseEntity<String> getCardsFromSection(@RequestParam(name="section") String section) throws JsonProcessingException {
        MarketplaceSection marketplaceSection = null;

        // Attempt to get the section enum (string is made uppercase to get correct value).
        try {
            marketplaceSection = MarketplaceSection.valueOf(section.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Bad section parameter input.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Section does not exist.");
        }

        List<Card> cards = cardRepository.findAllBySection(marketplaceSection);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(cards));
    }


    /**
     * GET endpoint, returns detailed information about a single card given an ID
     *
     * Preconditions: Given card ID is of type Long and is for a card that exists in database
     * Postconditions: Card information is returned in HTTP request
     *
     * @param cardId ID of card to be retrieved from DB
     * @return 200 if valid card, 400 if bad formatted ID, 401 if unauthorized, 406 if ID isn't in DB
     * @throws JsonProcessingException if mapper to convert the response into a JSON string fails.
     */
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<String> getCardById (@PathVariable Long cardId) throws JsonProcessingException {
        Card card = cardRepository.findCardById(cardId);
        if(card == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(card));
    }

}
