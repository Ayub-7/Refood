package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.*;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.models.responses.CardIdResponse;
import org.seng302.repositories.CardRepository;
import org.seng302.repositories.UserRepository;
import org.seng302.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Controller class that handles the endpoints of community marketplace cards.
 */
@RestController
public class CardController {

    private static final Logger logger = LogManager.getLogger(CardController.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    public CardController(UserRepository userRepository, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }


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

    /**
     * GET endpoint, retrieves all the expired cards of a user and
     * sends expired card notifications to the frontend to be displayed on the users home page feed.
     * @param userId
     * @return A list of notifications and code 200, 401 or 403.
     * @throws JsonProcessingException
     */
    @GetMapping("/users/{userId}/cards/notifications")
    public ResponseEntity<String> getExpiredCards (@PathVariable Long userId, HttpSession session) throws JsonProcessingException {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        if (currentUser.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Notification> notifications = notificationRepository.findNotificationsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(notifications));
    }

    public static Date getDate() {
        return new Date();
    }

    /**
     * Checks for expired cards and creates notification objects for each expired card without a current notification.
     * Function is run every 10 minutes.
     *
     *
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 0)
    public void updateExpiredCards() {
        logger.info("Checking for expired cards");
        Date date = new Date();
        List<Card> expiredCards = cardRepository.findAllByDisplayPeriodEndBefore(date);
        //temp
        System.out.println(expiredCards);
        for (Card card: expiredCards) {
            Notification exists = notificationRepository.findNotificationByCardId(card.getId());
            if (exists == null) {
                Long userId = card.getUser().getId();
                Long cardId = card.getId();
                String title = card.getTitle();
                Date displayPeriodEnd = card.getDisplayPeriodEnd();
                Notification notification = new Notification(userId, cardId, title, displayPeriodEnd);

                notificationRepository.save(notification);
            } else {
                if(checkCardIsPastNotificationPeriod(card.getDisplayPeriodEnd())) {
                    deleteExpiredCard(card);
                }
            }
        }
        int repositorySize = notificationRepository.findAll().size();
        String message = MessageFormat.format("Number of expired cards: {0}", repositorySize);
        logger.info(message);
    }


    /**
     * Deletes given card and sets notification status to deleted
     * Preconditions: Card exists and has notification that is expired
     * Postconditions: Card is removed from DB and Cards notification is updated to deleted
     * @param card card that is going to be deleted
     */
    private void deleteExpiredCard(Card card) {
        Notification cardNotification = notificationRepository.findNotificationByCardId(card.getId());
        cardNotification.setDeleted();
        notificationRepository.save(cardNotification);

        cardRepository.delete(card);
    }


    /**
     * Checks card's display period end to see if it has been 24 hours
     * Preconditions: Card exists with valid display period
     * Postconditions: Card will either be past notification period or not
     * @param displayPeriodEnd cards display period end, which would be when the notification starts
     * @return True if notification has existed for 24 hours or longer, False if existed for less
     */
    private boolean checkCardIsPastNotificationPeriod(Date displayPeriodEnd) {
        Date now = new Date();

        long timeDiffInMs = Math.abs(now.getTime() - displayPeriodEnd.getTime());
        long timeDiff = TimeUnit.HOURS.convert(timeDiffInMs, TimeUnit.MILLISECONDS);

        return timeDiff >= 24;
    }


    /**
     * GET endpoint, returns detailed information about a cards belonging to a specific User
     *
     * Preconditions: User ID given is for a user that exists
     * Postconditions: All cards belonging to the user are returned
     *
     * @param userId ID of user whose cards we want to retrieve
     * @return 200 if valid user, 400 if bad formatted ID, 401 if unauthorized, 406 if user doesn't exist
     * @throws JsonProcessingException if mapper to convert the response into a JSON string fails.
     */
    @GetMapping("/users/{userId}/cards")
    public ResponseEntity<String> getUserCards (@PathVariable Long userId) throws JsonProcessingException {
        User user = userRepository.findUserById(userId);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        List<Card> cards = cardRepository.findCardsByUser(user);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(cards));
    }

    /**
     * PUT endpoint, extends cards display period by two weeks
     *
     * Preconditions: User must be logged in, User must be the creator of the card, the card must exist
     * Postconditions: Card display period is extended by two weeks
     *
     * @param cardId Id of the card that is going to be extended
     * @param session User session of user that is updating card
     * @return 200 if updated, 406 if ID does not exist, 401 if unauthorized, 403 if not creator or GAA
     * @throws JsonProcessingException if mapper to convert the response into a JSON string fails.
     */
    @PutMapping("/cards/{cardId}/extenddisplayperiod")
    public ResponseEntity<String> extendDisplayPeriodById (@PathVariable Long cardId, HttpSession session) throws JsonProcessingException {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        Card card = cardRepository.findCardById(cardId);
        // Attempting to create a copy of a card that does not exist
        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        // Attempting to create a card for somebody else.
        if (card.getUser().getId() != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // Creating a copy of the old card with extended date
        card.updateDisplayPeriodEndDate();

        //Deletes cards notification (since display period is being extended)
        Notification cardNotification = notificationRepository.findNotificationByCardId(cardId);
        notificationRepository.delete(cardNotification);

        cardRepository.save(card);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * DELETE endpoint, deletes a single card given an ID
     *
     * Preconditions: Given card ID is of type Long is for a card that exists in database and the logged in user is
     * the creator the given card
     * Postconditions: Card is deleted from the database
     *
     * @param cardId ID of card to be retrieved from DB
     * @param session the current user session
     * @return 401 if not logged in (handled by spring sec), 403 if creatorId, session user Id do not match or if not a D/GAA,
     * 400 if there are errors with data, 200 otherwise.
     * @throws JsonProcessingException if mapper to convert the response into a JSON string fails.
     */
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<String> deleteCardById (@PathVariable Long cardId, HttpSession session) throws JsonProcessingException {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        Card card = cardRepository.findCardById(cardId);
        if(card == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        User cardCreator = card.getUser();
        // Attempting to create a card for somebody else.
        if(cardCreator.getId() != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //Deletes cards notification (since display period is being extended)
        Notification cardNotification = notificationRepository.findNotificationByCardId(cardId);
        if(cardNotification != null) notificationRepository.delete(cardNotification);

        cardRepository.deleteCardById(cardId);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(card));
    }

    /**
     * PUT card endpoint; updates/modifies an existing card with new information.
     *
     * @param cardId ID of card to modify
     * @param cardInfo the edited information of the card to save
     * @param session the current user session
     * @return 401 if not logged in (handled by spring sec), 403 if card owner ID, session user ID do not match or if not a D/GAA,
     * 406 if the card ID does not exist, 400 if there are errors with the supplied information, 200 otherwise.
     */
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<String> editCardById(@PathVariable long cardId, @RequestBody NewCardRequest cardInfo, HttpSession session) {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        Card editedCard;
        try { // Attempt to create a card entity with the given info.
            editedCard = new Card(cardInfo, currentUser);
        }
        catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        Card existingCard = cardRepository.findCardById(cardId);
        if (existingCard == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        if (existingCard.getUser().getId() != currentUser.getId() && !Role.isGlobalApplicationAdmin(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        editedCard.setId(cardId);
        cardRepository.save(editedCard);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
