package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.Listing;
import org.seng302.models.ListingLike;
import org.seng302.models.User;
import org.seng302.repositories.ListingLikeRepository;
import org.seng302.repositories.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
}
