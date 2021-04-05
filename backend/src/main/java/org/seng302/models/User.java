package org.seng302.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.seng302.models.requests.NewUserRequest;
import org.seng302.utilities.BusinessesAdministeredListSerializer;
import org.seng302.utilities.Encrypter;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter @Setter // generate setters and getters for all fields (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String bio;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private String homeAddress;
    @JsonIgnore
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "administrators")
    @JsonSerialize(using = BusinessesAdministeredListSerializer.class)
    private List<Business> businessesAdministered;


    protected User() {}

    /**
     * Constructor for a user object.
     * @param firstName given first name of user.
     * @param middleName middle name of the user.
     * @param lastName surname/last name of user.
     * @param nickname alternative name.
     * @param bio a short description of the user.
     * @param email the email address.
     * @param dateOfBirth birth date in yyyy-mm-dd format
     * @param phoneNumber phone number.
     * @param homeAddress current address of the user.
     * @param password (encrypted) password of user.
     */
    public User(String firstName, String middleName, String lastName, String nickname, String bio, String email, String dateOfBirth, String phoneNumber, String homeAddress, String password) throws NoSuchAlgorithmException {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.bio = bio;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        this.password = Encrypter.hashString(password);
        this.created = new Date();
        this.role = Role.USER;
    }

    /**
     * Constructor taking in a registration request.
     * @param req NewUserRequest object containing inputted registration data.
     * @throws NoSuchAlgorithmException if hashing fails.
     */
    public User(NewUserRequest req) throws NoSuchAlgorithmException {
        this.firstName = req.getFirstName();
        this.middleName = req.getMiddleName();
        this.lastName = req.getLastName();
        this.nickname = req.getNickname();
        this.bio = req.getBio();
        this.email = req.getEmail();
        this.dateOfBirth = req.getDateOfBirth();
        this.phoneNumber = req.getPhoneNumber();
        this.homeAddress = req.getHomeAddress();
        this.password = req.getPassword();
        newRegistration();
    }

    /**
     * Alternative constructor with just the barebones fields required. Used to create a DGAA.
     * @param email
     * @param password
     * @param role designated website role of user.
     */
    public User(String email, String password, Role role) throws NoSuchAlgorithmException {
        this.email = email;
        this.role = role;
        this.created = new Date();
        this.password = Encrypter.hashString(password);
    }

    /**
     * Method when a JSON of a registration is requested, and specific values are needed to be changed before
     */
    public void newRegistration() throws NoSuchAlgorithmException {
        this.created = new Date();
        this.password = Encrypter.hashString(this.password);
        this.role = Role.USER;
    }

}
