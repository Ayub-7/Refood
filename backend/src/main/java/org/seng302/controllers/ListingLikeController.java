package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.Listing;
import org.seng302.models.ListingLike;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.repositories.ListingLikeRepository;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for endpoints relating to listing likes
 */
@RestController
public class ListingLikeController {

    private static final Logger logger = LogManager.getLogger(ListingLikeController.class.getName());

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ListingLikeRepository listingLikeRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * POST endpoint - Adds a new like to a business sale listing.
     * @param id of the sale listing to add a new like to.
     * @param session the current active user session.
     * @return 401 if unauthorized, 406 if the listing does not exist, 201 otherwise.
     */
    @PostMapping("/businesses/listings/{id}/like")
    public ResponseEntity<String> addLikeToListing(@PathVariable long id, HttpSession session) {
        Listing listing = listingRepository.findListingById(id);

        if (listing == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        User user = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        ListingLike like = new ListingLike(user, listing);
        listingLikeRepository.save(like);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Retrieves and returns a list of LISTINGS that the user has liked.
     * @param id unique identifier of the user.
     * @param session current active user session.
     * @return 401 if unauthorized, 403 if forbidden (not dgaa or correct user), 406 if the user does not exist.
     * 200 otherwise, may return an empty list (because the user has not liked anything).
     */
    @GetMapping("/users/{id}/likes")
    public ResponseEntity<List<Listing>> getUserLikedListings(@PathVariable long id, HttpSession session) {
        User user = userRepository.findUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        User sessionUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        if (user.getId() != sessionUser.getId() && !Role.isGlobalApplicationAdmin(sessionUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ListingLike> likedListings = listingLikeRepository.findListingLikesByUserId(id);
        List<Listing> listings = likedListings.stream().map(ListingLike::getListing).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(listings);
    }
}
