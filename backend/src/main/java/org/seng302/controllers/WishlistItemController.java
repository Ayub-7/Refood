package org.seng302.controllers;

import org.seng302.models.*;
import org.seng302.repositories.*;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controlled class containing restful api calls regarding users and their wish listed businesses.
 */
@RestController
public class WishlistItemController {

    @Autowired
    WishlistItemRepository wishlistItemRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * GET request called to get the businesses on a users wishlist. Error response returned when user is not
     * logged in
     * @param session logged in users session
     * @param id Users id
     * @return ResponseEntity with relevant http status
     */
    @GetMapping("/users/{id}/wishlist")
    public ResponseEntity<List<WishlistItem>> getWishlistBusiness(@PathVariable long id, HttpSession session) {
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        User user = userRepository.findUserById(id);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        List<WishlistItem> wishlistItems = wishlistItemRepository.findWishlistItemsByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(wishlistItems);
    }

    /**
     * Post request called when a user attempts to add a business to their wishlist. Error response returned when attempting
     * to wishlist a business with id not found in the repository.
     * @param businessId Id of the business to wishlist
     * @param session
     * @return ResponseEntity with relevant http status
     */
    @PostMapping("/businesses/{businessId}/wishlist")
    public ResponseEntity<String> wishlistBusiness(@PathVariable long businessId, HttpSession session) {
        User user = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        long userId = user.getId();
        Business business = businessRepository.findBusinessById(businessId);
        if (business == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        WishlistItem wishlistItem = new WishlistItem(userId, businessId);
        wishlistItemRepository.save(wishlistItem);
        return ResponseEntity.status(HttpStatus.CREATED).body("Business added to wishlist");
    }

    /**
     * Delete request, to remove a business from a users wishlist
     * @param id Id of the wishlistItem object
     * @param session
     * @return ResponseEntity with relevant http status
     */
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<String> removeBusinessFromWishlist(@PathVariable long id, HttpSession session) {
        User user = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        WishlistItem wishlistItem = wishlistItemRepository.findWishlistItemById(id);
        if (wishlistItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else if (wishlistItem.getUserId() != user.getId() && user.getRole() == Role.USER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            wishlistItemRepository.delete(wishlistItem);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
