package org.seng302.models.requests;

import org.seng302.models.Address;

/**
 * DTO class that holds a modified user info.
 */
public class ModifyUserRequest extends UserRequest {

    public ModifyUserRequest(String firstName, String middleName, String lastName, String nickname, String bio, String email, String dateOfBirth, String phoneNumber, Address homeAddress, String password) {
        super(firstName, middleName, lastName, nickname, bio, email, dateOfBirth, phoneNumber, homeAddress, password);
    }
}
