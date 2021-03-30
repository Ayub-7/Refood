package org.seng302.models.responses;

import lombok.Data;
import org.seng302.models.Role;
/**
 * A simple DTO which holds the user id to transfer to frontend.
 */
@Data
public class UserIdResponse {

    private Long userId;
    private Role role;

    public UserIdResponse(Long userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public UserIdResponse(Long userId){
        this.userId = userId;
    }


}
