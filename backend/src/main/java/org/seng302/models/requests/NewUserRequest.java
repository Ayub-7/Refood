package org.seng302.models.requests;

import lombok.Data;
import org.seng302.models.Address;

/**
 * DTO class that holds a new user registration info.
 */
public class NewUserRequest extends UserRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String bio;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;

    private Address homeAddress;
    private String password;

    public NewUserRequest(String firstName, String middleName, String lastName, String nickname, String bio, String email, String dateOfBirth, String phoneNumber, Address homeAddress, String password) {
        super(firstName, middleName, lastName, nickname, bio, email, dateOfBirth, phoneNumber, homeAddress, password);
    }
}
