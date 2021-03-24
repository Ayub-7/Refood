package org.seng302.models.responses;

import lombok.Data;

/**
 * A simple DTO which holds the user id to transfer to frontend.
 */
@Data
public class UserIdResponse {

    private Long userId;

    public UserIdResponse(Long userId) {
        this.userId = userId;
    }

}
