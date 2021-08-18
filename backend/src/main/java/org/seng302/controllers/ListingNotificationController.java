package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.seng302.models.*;
import org.seng302.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ListingNotificationController {

  @Autowired
  private ListingRepository listingRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ListingLikeRepository listingLikeRepository;

  @Autowired
  private ListingNotificationRepository listingNotificationRepository;

  @Autowired
  private BoughtListingRepository boughtListingRepository;

  /**
   * Endpoint for creating a notification for a listing that's just been purchased.
   * @param listingId ID of the listing
   * @return 201 if successful, 406 if listing or receiver are null
   */
  @PostMapping("/listings/{listingId}/notify")
  public ResponseEntity<String> addNotificationToListing(@PathVariable("listingId") long listingId,
                                                         HttpSession session) {
    User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
    Listing listing = listingRepository.findListingById(listingId);
    if (listing == null || currentUser == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    Inventory inventory = listing.getInventoryItem();
    BoughtListing boughtListing = new BoughtListing(currentUser, inventory.getProduct(), listing.getLikes(), listing.getQuantity(), listing.getCreated(), listing.getId(), listing.getPrice());
    boughtListingRepository.save(boughtListing);
    NotificationStatus status = NotificationStatus.BOUGHT;
    List<ListingLike> userLikes = listingLikeRepository.findListingLikeByListingId(listingId);
    List<User> receivers = userLikes.stream().map(ListingLike::getUser).collect(Collectors.toList());
    for (User receiver : receivers) {
      ListingNotification listingNotification = new ListingNotification(receiver, boughtListing, status);
      listingNotificationRepository.save(listingNotification);
    }
    ListingNotification listingNotification = new ListingNotification(currentUser, boughtListing, status);
    listingNotificationRepository.save(listingNotification);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Endpoint for retrieving a list of a user's listing notifications
   * @param userId ID of the user
   * @param session the current active user session
   * @return 200 if successful with a list of listing notifications
   * @throws JsonProcessingException when json mapping object to a json string fails unexpectedly
   */
  @GetMapping("users/{userId}/notifications")
  public ResponseEntity<List<ListingNotification>> getUserListingNotifications(@PathVariable("userId") long userId,
                                                                           HttpSession session) throws JsonProcessingException {
    User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
    User user = userRepository.findUserById(userId);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    if (currentUser.getId() != user.getId()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    List<ListingNotification> listingNotifications = listingNotificationRepository.findListingNotificationsByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(listingNotifications);
  }
}
