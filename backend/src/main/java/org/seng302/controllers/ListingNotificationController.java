package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.models.Listing;
import org.seng302.models.ListingNotification;
import org.seng302.models.NotificationStatus;
import org.seng302.models.User;
import org.seng302.models.requests.ListingNotificationRequest;
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
  private ListingRepository listingRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ListingNotificationRepository listingNotificationRepository;

  @PostMapping("/users/{id}/notify")
  public ResponseEntity<String> addNotificationToListing(@PathVariable long id, @RequestBody ListingNotificationRequest request) {
    User receiver = userRepository.findUserById(id);
    Listing listing = listingRepository.findListingById(request.getListingID());
    NotificationStatus status = request.getStatus();
    if (listing == null) {
      logger.error("listing invalid");
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    ListingNotification listingNotification = new ListingNotification(receiver, listing, status);
    listingNotificationRepository.save(listingNotification);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
