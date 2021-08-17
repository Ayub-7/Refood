package org.seng302.controllers;

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
  private UserRepository userRepository;

  @Autowired
  private ListingLikeRepository listingLikeRepository;

  @Autowired
  private ListingNotificationRepository listingNotificationRepository;

  @Autowired
  private BoughtListingRepository boughtListingRepository;

  @Autowired
  private BusinessRepository businessRepository;

  /**
   * Endpoint for creating a notification for a listing that's just been purchased.
   * @param listingId ID of the listing
   * @return 201 if successful, 406 if listing or receiver are null
   */
  @PostMapping("/listings/{listingId}/notify")
  public ResponseEntity<String> addNotificationToListing(@PathVariable("listingId") long listingId,
                                                         HttpSession session) {
    User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    BoughtListing boughtListing = boughtListingRepository.findBoughtListingByListingId(listingId);
    if (boughtListing == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    Business business = businessRepository.findBusinessById(boughtListing.getProduct().getBusinessId());
    NotificationStatus status = NotificationStatus.BOUGHT;
    List<ListingLike> userLikes = listingLikeRepository.findListingLikeByListingId(listingId);
    List<User> receivers = userLikes.stream().map(ListingLike::getUser).collect(Collectors.toList());
    for (User receiver : receivers) {
      ListingNotification listingNotification = new ListingNotification(receiver, boughtListing, status);
      listingNotificationRepository.save(listingNotification);
    }
    ListingNotification listingNotification = new ListingNotification(currentUser, business, boughtListing, status);
    listingNotificationRepository.save(listingNotification);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Endpoint for retrieving a list of a user's listing notifications
   * @param userId ID of the user
   * @param session the current active user session
   * @return 200 if successful with a list of listing notifications
   */
  @GetMapping("/users/{userId}/notifications")
  public ResponseEntity<List<ListingNotification>> getUserListingNotifications(@PathVariable("userId") long userId, HttpSession session) {
    User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
    User user = userRepository.findUserById(userId);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    if (currentUser.getId() != user.getId()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    List<ListingNotification> listingNotifications = listingNotificationRepository.findListingNotificationsByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(listingNotifications);
  }

  /**
   * Endpoint for retrieving a list of a business's listing notifications
   * @param businessId Business' ID
   * @return 200 if successful with a list of listing notifications
   */
  @GetMapping("/businesses/{businessId}/notifications")
  public ResponseEntity<List<ListingNotification>> getBusinessListingNotifications(@PathVariable("businessId") long businessId) {
    Business business = businessRepository.findBusinessById(businessId);

    if (business == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    List<ListingNotification> listingNotifications = listingNotificationRepository.findListingNotificationByBusinessId(businessId);
    return ResponseEntity.status(HttpStatus.OK).body(listingNotifications);
  }
}
