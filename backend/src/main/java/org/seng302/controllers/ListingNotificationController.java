package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.*;
import org.seng302.models.requests.ListingNotificationRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.ListingNotificationRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ListingNotificationController {

  private static final Logger logger = LogManager.getLogger(ListingNotificationController.class.getName());

  @Autowired
  private BusinessRepository businessRepository;

  @Autowired
  private ListingRepository listingRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ListingNotificationRepository listingNotificationRepository;

  /**
   * Endpoint for creating a notification for a listing that's just been purchased.
   * @param businessId ID of the business owning the listing
   * @param listingId ID of the listing
   * @param userId ID of the user
   * @param request json body with the userId, listingId, message and notification status
   * @return 201 if successful, 406 if listing or receiver are null
   */
  @PostMapping("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify")
  public ResponseEntity<String> addNotificationToListing(@PathVariable("businessId") long businessId,
                                                         @PathVariable("listingId") long listingId,
                                                         @PathVariable("userId") long userId,
                                                         @RequestBody ListingNotificationRequest request) {
    User receiver = userRepository.findUserById(userId);
    Listing listing = listingRepository.findListingById(listingId);
    Business business = businessRepository.findBusinessById(businessId);
    NotificationStatus status = request.getStatus();
    if (listing == null || receiver == null || business == null) {
      logger.error("Listing, receiver or business invalid");
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    ListingNotification listingNotification = new ListingNotification(receiver, listing, status);
    listingNotificationRepository.save(listingNotification);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
