package org.seng302.models.requests;

import lombok.Data;
import org.seng302.models.Address;

@Data
public class NewUserRequest {

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

}
