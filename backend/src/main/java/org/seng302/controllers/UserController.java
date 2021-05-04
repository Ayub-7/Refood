package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.finders.UserFinder;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.Business;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.requests.NewUserRequest;
import org.seng302.models.responses.UserIdResponse;
import org.seng302.repositories.UserRepository;
import org.seng302.utilities.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * REST controller for user related calls.
 */
@RestController
public class UserController {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

//    @Autowired
    private UserRepository userRepository;

//    @Autowired
    private UserFinder userFinder;

    @Autowired
    public UserController(UserRepository userRepository, UserFinder userFinder) {
        this.userRepository = userRepository;
        this.userFinder = userFinder;
    }

    /**
     * Get request mapping for get user information by Id
     * @param id the user's id in the database
     * @return ResponseEntity
     * @throws JsonProcessingException when
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
     * @param session
     * @return 200 if login is successful, 400 if email/password is invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) throws NoSuchAlgorithmException, JsonProcessingException {
        User existingUser = userRepository.findUserByEmail(loginRequest.getEmail());
        if (existingUser != null) {
            if (Encrypter.hashString(loginRequest.getPassword()).equals(existingUser.getPassword())) {
                UserIdResponse userIdResponse = new UserIdResponse(existingUser);
                session.setAttribute(User.USER_SESSION_ATTRIBUTE, existingUser);
//             if (loginRequest.getPassword().equals(existingUser.getPassword())) {
//                 UserIdResponse userIdResponse = new UserIdResponse(existingUser);
//                 session.setAttribute(User.USER_SESSION_ATTRIBUTE, existingUser);

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
    public ResponseEntity<User> checksession(HttpServletRequest req, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Business bbusiness = (Business) session.getAttribute("business");
        System.out.println(user.getId());
        System.out.println(bbusiness.getName());
        //System.out.println(business.getId());
//        System.out.println(business.Name);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }




    @PostMapping("/logoutuser")
    public ResponseEntity<String> logoutUser(HttpServletRequest req, HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
   }


    /**
     * This method inserts a new user into the user repository
     * @param user Body of request in API specific format
     * @return Status code depending on result of registration.
     */
    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody NewUserRequest user) throws JsonProcessingException, NoSuchAlgorithmException, ParseException {
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            List<String> registrationErrors = registrationUserCheck(user);
            if (registrationErrors.size() == 0) { // No errors found.
                User newUser = new User(user);
                userRepository.save(newUser);

                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
                SecurityContextHolder.getContext().setAuthentication(auth);

                UserIdResponse res = new UserIdResponse(newUser);
                String jsonString = mapper.writeValueAsString(res);

                return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
            else {
                logger.error("Invalid registration.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors with registration details: " + registrationErrors);
            }
        }
        logger.error("Registration email address already in use.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use.");
    }

    /**
     * This method checks whether all the mandatory fields of the new user are present.
     * @param user The user to check the validity of
     * @return list of errors with the new registration request - if there is any.
     */
    public List<String> registrationUserCheck(NewUserRequest user) throws ParseException {
        List<String> errors = new ArrayList<>();

        if (user.getFirstName() == null || (user.getFirstName() != null && user.getFirstName().length() == 0)) {
            errors.add("First Name");
        }
        if (user.getLastName() == null || (user.getLastName() != null && user.getLastName().length() == 0)) {
            errors.add("Last Name");
        }
        if (user.getEmail() == null || !this.isValidEmail(user.getEmail()) || (user.getEmail() != null && user.getEmail().length() == 0)) {
            errors.add("Email");
        }
        if (user.getPassword() == null || (user.getPassword() != null && user.getPassword().length() == 0)) {
            errors.add("Password");
        }
        if (user.getDateOfBirth() == null || !this.isNotUnderage(user.getDateOfBirth()) || (user.getDateOfBirth() != null && user.getDateOfBirth().length() == 0)) {
            errors.add("Date of Birth");
        }
        if (user.getHomeAddress() == null) {
            errors.add("Home Address");
        }
        else if (user.getHomeAddress().getCountry() == null || (user.getHomeAddress().getCountry() != null && user.getHomeAddress().getCountry().length() == 0)) {
            errors.add("Home Address - Country");
        }

        return errors;
    }

    /**
     * Takes an email as a parameter to check if it is in the right format.
     * @param email The email to be checked
     * @return True if it is in a valid format; otherwise, false
     */
    public boolean isValidEmail(String email) {
        Pattern re = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher m = re.matcher(email);
        return m.matches();
    }

    /**
     * Checks that the user is not underage.
     * @param date The date of birth of the user.
     * @return True if not underage; otherwise, false.
     * @throws ParseException
     */
    public boolean isNotUnderage(String date) throws ParseException {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Date present = new SimpleDateFormat("dd/MM/yyyy").parse(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));
        long diffInMillies = present.getTime() - dob.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 365;
        return diff >= 13;
    }

    /**
     * This method retrieves user information by name.
     * @param query Body of the incoming request.
     * @return Http status code and list of users with name/names matching request.
     */
    @GetMapping("/users/search")
    public  ResponseEntity<String> searchUser(@RequestParam(name="searchQuery") String query) throws JsonProcessingException {
        System.out.println("search called");
        List<User> users = userFinder.queryByName(query);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(users));
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
     * @return status code. 200 if it works, 409 if the DGAA is revoking their own role, 406 if bad id, 401 if missing session token, 403 if incorrect role (last two handled by spring sec).
     */
    @PutMapping("/users/{id}/revokeAdmin")
    public ResponseEntity<String> revokeUserAdmin(@PathVariable Long id, HttpSession session) {
        if (userRepository.findUserById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        User self = (User) session.getAttribute("user");
        if (self != null) {
            if (self.getId() == id) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        userRepository.updateUserRole(id, Role.USER);
        return ResponseEntity.status(HttpStatus.OK).build();

    }



}
