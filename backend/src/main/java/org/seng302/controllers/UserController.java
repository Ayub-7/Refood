package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seng302.finders.UserFinder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hibernate.annotations.Filter;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.requests.NewUserRequest;
import org.seng302.models.responses.UserIdResponse;
import org.seng302.repositories.UserRepository;
import org.seng302.utilities.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * REST controller for user related calls.
 */
@RestController
public class UserController {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFinder userFinder;

    /**
     * Get request mapping for get user information by Id
     * @param id the user's id in the database
     * @return ResponseEntity
     * @throws JsonProcessingException
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) throws JsonProcessingException {
        User user = userRepository.findUserById(Long.parseLong(id));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else {
            String userJson = mapper.writeValueAsString(user);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userJson);
        }
    }

    /**
     * Login POST method. Checks if user exists and provided details are correct and authenticates if true.
     * @param loginRequest a login request containing the email and password.
     * @return 200 if login is successful, 400 if email/password is invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest req, HttpSession session) throws NoSuchAlgorithmException, JsonProcessingException {
        User existingUser = userRepository.findUserByEmail(loginRequest.getEmail());
        if (existingUser != null) {
            if (loginRequest.getPassword().equals(existingUser.getPassword())) {
                UserIdResponse userIdResponse = new UserIdResponse(existingUser.getId(), existingUser.getRole());
                session.setAttribute("user", existingUser);

                Authentication auth = new UsernamePasswordAuthenticationToken(existingUser.getEmail(), existingUser.getPassword(), AuthorityUtils.createAuthorityList("ROLE_" + existingUser.getRole()));
                SecurityContextHolder.getContext().setAuthentication(auth);

                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(userIdResponse));

            }
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * THIS NEEDS TO BE REMOVED AT SOME POINT - HERE FOR TESTING PURPOSES.
     * Prints out the current user session/authentication details into console.
     */
    @GetMapping("/checksession")
    public void checksession(HttpServletRequest req, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("--------------------------------------------------");
        System.out.println("PRINCIPAL: " + auth.getPrincipal());
        System.out.println("CREDS: " + auth.getCredentials());
        System.out.println("DETAILS: " + auth.getDetails());
        System.out.println("AUTH: " + auth.getAuthorities());

    }


    /**
     * This method inserts a new user into the user repository
     * @param user Body of request in API specific format
     * @return Status code depending on result of registration.
     */
    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody NewUserRequest user) throws JsonProcessingException, NoSuchAlgorithmException {

        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            if (isValidUser(user)) {
                User newUser = new User(user);
                System.out.println(user.getPassword());
                System.out.println(newUser.getPassword());
                userRepository.save(newUser);

                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
                SecurityContextHolder.getContext().setAuthentication(auth);

                UserIdResponse res = new UserIdResponse(newUser.getId());
                String jsonString = mapper.writeValueAsString(res);

                return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        System.out.println("Bad email");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * This method retrieves user information by name.
     * @param query Body of the incoming request.
     * @return Http status code and list of users with name/names matching request.
     */
    @GetMapping("/users/search")
    public  ResponseEntity<String> searchUser(@RequestParam(name="searchQuery") String query) throws JsonProcessingException {

        List<User> users = userFinder.queryByName(query);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(users));
    }

    /**
     * This method checks whether all the mandatory fields of the new user are present.
     * @param user The user to check the validity of
     * @return boolean If the users mandatory fields are present
     */
    public Boolean isValidUser(NewUserRequest user) {
        return user.getFirstName() != null && user.getLastName() != null && user.getDateOfBirth() != null && user.getHomeAddress() != null && user.getEmail() != null;
    }

    // -- ADMIN REQUESTS

    /**
     * Method to let a DGAA user make a user an GAA admin user.
     * @param id user id to be made admin.
     * @return status code. 200 if it works, 406 if bad id, 401 if missing session token, 403 if no authority (last two handled by spring sec).
     */
    @PutMapping("/users/{id}/makeAdmin")
    public ResponseEntity<String> makeUserAdmin(@PathVariable long id) {
        if (userRepository.findUserById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        userRepository.updateUserRole(id, Role.GAA);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method to let a DGAA user revoke GAA admin status from another user. Reverts the user back to USER role.
     * @param id user id to revoke admin user role.
     * @return status code. 200 if it works, 406 if bad id, 401 if missing session token, 403 if no authority (last two handled by spring sec).
     */
    @PutMapping("/users/{id}/revokeAdmin")
    public ResponseEntity<String> revokeUserAdmin(@PathVariable Long id) {
        if (userRepository.findUserById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        userRepository.updateUserRole(id, Role.USER);
        return ResponseEntity.status(HttpStatus.OK).build();

    }


}
