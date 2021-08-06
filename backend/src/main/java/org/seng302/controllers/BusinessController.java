package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.finders.BusinessFinder;
import org.seng302.finders.AddressFinder;
import org.seng302.models.Business;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.responses.BusinessIdResponse;
import org.seng302.models.requests.NewBusinessRequest;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.models.requests.BusinessIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class BusinessController {

    private static final Logger logger = LogManager.getLogger(BusinessController.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessFinder businessFinder;

    @Autowired
    private AddressFinder addressFinder;


    /**
     * Get request mapping for getting business by id
     * @param id the business's id
     * @return ResponseEntity - 401 when unauthorized (handled by spring sec). 406 when business doesn't exist. 200 otherwise.
     * @throws JsonProcessingException when json mapping object to a json string fails unexpectedly.
     */
    @GetMapping("/businesses/{id}")
    public ResponseEntity<String> getBusiness(@PathVariable String id) throws JsonProcessingException {
        Business business = businessRepository.findBusinessById(Long.parseLong(id));
        if (business == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        String businessJson = mapper.writeValueAsString(business);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(businessJson);
    }

    /**
     * Post request mapping to create a new business.
     * @param req DTO containing the new request information.
     * @param session the current user session.
     * @return ResponseEntity 401 if no auth (handled by spring sec), 400 if there are errors in the request, 201 otherwise.
     */
    @PostMapping("/businesses")
    public ResponseEntity<String> createBusiness(@RequestBody NewBusinessRequest req, HttpSession session) throws JsonProcessingException {
        Business business = new Business(req.getName(), req.getDescription(), req.getAddress(), req.getBusinessType());

        User owner = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        business.createBusiness(owner);

        if (isValidBusiness(business)) {
            businessRepository.save(business);
            BusinessIdResponse businessIdResponse = new BusinessIdResponse(business.getId(), business.getBusinessType());

            String jsonString = mapper.writeValueAsString(businessIdResponse);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jsonString);
        } else {
            logger.debug(owner.getId());
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

    /**
     * A PUT request that removes a user from being an administrator of a given business.
     * @param userIdRequest DTO class that contains the user id to remove from the business.
     * @param id the unique business id.
     * @param session current user session.
     * @return 406 if business doesn't exist, 403 if user making the request is not the primary admin, or DGAA,
     * 400 if there is an error in user being removed, 200 otherwise.
     */
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

    /**
     * Retrieves the business information, and stores the information in the session for the user to act as.
     * @param businessIdRequest DTO containing the buinessId to make the user act as.
     * @param session current authenticated login session of the user.
     * @return a response entity with the appropriate status code.
     */
    @PostMapping("/actasbusiness")
    public ResponseEntity<String> actAsBusiness(@RequestBody BusinessIdRequest businessIdRequest, HttpSession session) {
        long businessId = businessIdRequest.getBusinessId();
        if(businessId == 0){
            session.setAttribute(Business.BUSINESS_SESSION_ATTRIBUTE, null);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            Business existingBusiness = businessRepository.findBusinessById(businessId);
            if (existingBusiness != null) {
                session.setAttribute(Business.BUSINESS_SESSION_ATTRIBUTE, existingBusiness);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    /**
     * Checks the current session of the currently controlled business.
     * @param session business' current session
     * @return ResponseEntity containing the business object for the current session
     */
    @GetMapping("/checkbusinesssession")
    public ResponseEntity<Business> checkBusinessSession(HttpSession session) {
        Business business = (Business) session.getAttribute("business");
        if (business != null) {
            return ResponseEntity.status(HttpStatus.OK).body(business);
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        }
    }

    /**
     * Searches for businesses, with credintials
     * @param query A string with the search's query
     * @param type Type of business
     * @return Http status code and list of businesses with name/names matching request.
     */
    @GetMapping("/businesses/search")
    public ResponseEntity<String> findBusinesses(@RequestParam(name="query") String query, @RequestParam(name="type") String type, HttpSession session) throws JsonProcessingException {
        logger.debug("Searching for businesses...");
        System.out.println("Searching for businesses...");
        List<Business> businesses = removeBusinessesAdministered(businessFinder.findBusinesses(query, type));

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(businesses));
    }
    @GetMapping("/businesses/location")
        public ResponseEntity<String> findBusinesses(@RequestParam(name="query") String query, HttpSession session) throws JsonProcessingException {
            logger.debug("Searching for businesses...");
            System.out.println("Searching for businesses...");
            Specification<Business> specification = addressFinder.findAddress(query);

            List<Business> businesses = removeBusinessesAdministered(businessRepository.findAll(specification));

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(businesses));
        }


    /**
     * Sets businesses administered to null for each admin, there is an issue with how our backend serializes business objects and if it encountered
     * a business object inside there it would replace every other instance of that object with the business name
     * @param businesses List of businesses that needs businessesAdministered removed
     * @return List of businesses with businessesAdministered field set to null
     */
    private List<Business> removeBusinessesAdministered(List<Business> businesses) {
        logger.debug("Removing businessesAdministered...");
        for(Business business: businesses) {
            List<User> admins = business.getAdministrators();
            for(User admin: admins) {
                admin.setBusinessesAdministered(null);
            }
        }

        logger.debug("businessesAdministered removed");
        return businesses;
    }
}
