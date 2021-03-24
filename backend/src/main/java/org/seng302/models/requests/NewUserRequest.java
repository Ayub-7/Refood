package org.seng302.models.requests;

import lombok.Data;

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
    private String homeAddress;
    private String password;

}
