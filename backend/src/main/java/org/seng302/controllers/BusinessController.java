package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seng302.models.Business;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.NewBusinessRequest;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class BusinessController {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get request mapping for getting business by id
     * @param id the business's id
     * @return ResponseEntity
     * @throws JsonProcessingException when json mapping object to a json string fails unexpectedly.
     */
    @GetMapping("/businesses/{id}")
    public ResponseEntity<String> getBusiness(@PathVariable String id) throws JsonProcessingException {
        Business business = businessRepository.findBusinessById(Long.parseLong(id));
        if (business == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else {
            String businessJson = mapper.writeValueAsString(business);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(businessJson);
        }
    }

    /**
     * Post request mapping to create a new Business
     * @param req Request body
     * @return ResponseEntity
     */
    @PostMapping("/businesses")
    public ResponseEntity<String> createBusiness(@RequestBody NewBusinessRequest req, HttpSession session) {
        Business business = new Business(req.getName(), req.getDescription(), req.getAddress(), req.getBusinessType());
        System.out.println("hello");
        User owner = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        business.createBusiness(owner);

        if (isValidBusiness(business)) {
            businessRepository.save(business);
            System.out.println("Valid business");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            System.out.println(owner.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Verifies that the request body to create a business is valid (currently defined as all values are not null)
     * @param business The business object being created
     * @return True if business is valid, else returns False
     */
    public boolean isValidBusiness(Business business) {
        return (business.getName() != null && business.getBusinessType() != null);
    }

    /**
     * A PUT request that makes a user an administrator of a given business.
     * @param userIdRequest DTO containing the userId to make admin of business.
     * @param id the unique business Id.
     * @param session current authenticated login session of the user.
     * @return a response entity with the appropriate status code.
     */
    @PutMapping("/businesses/{id}/makeAdministrator")
    public ResponseEntity<String> makeUserBusinessAdministrator(@RequestBody UserIdRequest userIdRequest, @PathVariable long id, HttpSession session) {
        long userId = userIdRequest.getUserId();
        User owner = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

        Business business = businessRepository.findBusinessById(id);

        if (business == null) { // 406 business non-existent.
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        // 403 if user making request is not primary admin or DGAA.
        if (business.getPrimaryAdministrator().getId() != (owner.getId()) && !owner.getRole().equals(Role.DGAA)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User userToAdmin = userRepository.findUserById(userId);
        if (userToAdmin == null || business.getAdministrators().contains(userToAdmin)) { // 400 if user is non-existent or is already admin.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        business.getAdministrators().add(userToAdmin);
        businessRepository.save(business);
        return ResponseEntity.status(HttpStatus.OK).build(); // 200
    }

    @PutMapping("/businesses/{id}/removeAdministrator")
    public ResponseEntity<String> removeUserBusinessAdministrator(@RequestBody UserIdRequest userIdRequest, @PathVariable long id, HttpSession session) {
        long userId = userIdRequest.getUserId();
        User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        Business business = businessRepository.findBusinessById(id);

        if (business == null) { // 406 business non-existent.
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        // 403 if user making request is not primary admin or DGAA.
        if (business.getPrimaryAdministrator().getId() != (currentUser.getId()) && !currentUser.getRole().equals(Role.DGAA)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User userToRevoke = userRepository.findUserById(userId);
        // 400 if user is non-existent, is not an admin, or is the primary admin.
        if (userToRevoke == null || !business.getAdministrators().contains(userToRevoke) || business.getPrimaryAdministrator().getId() == userToRevoke.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        business.getAdministrators().remove(userToRevoke);
        businessRepository.save(business);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
