package org.seng302.models.requests;

import lombok.Data;
import org.seng302.models.NotificationStatus;

/**
 * DTO request class that holds the information of a notification to send to users.
 */
@Data
public class ListingNotificationRequest {
  private long senderID;
  private long listingID;
  private String message;
  private NotificationStatus status;

  public ListingNotificationRequest(long senderID, long listingID, String message, NotificationStatus status) {
    this.senderID = senderID;
    this.listingID = listingID;
    this.message = message;
    this.status = status;
    }
}
