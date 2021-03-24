package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seng302.models.Business;
import org.seng302.models.User;
import org.seng302.models.requests.NewBusinessRequest;
import org.seng302.repositories.BusinessRepository;
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

    /**
     * Get request mapping for getting business by id
     * @param id the business's id
     * @return ResponseEntity
     * @throws JsonProcessingException
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
     * @throws JsonProcessingException
     */
    @PostMapping("/businesses")
    public ResponseEntity<String> createBusiness(@RequestBody NewBusinessRequest req, HttpSession session) {
        Business business = new Business(req.getName(), req.getDescription(), req.getAddress(), req.getBusinessType());
        User owner = (User) session.getAttribute("user");
        business.createBusiness(owner);

        if (isValidBusiness(business)) {
            businessRepository.save(business);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Verifies that the request body to create a business is valid (currently defined as all values are not null)
     * @param business The business object being created
     * @return True if business is valid, else returns False
     */
    public boolean isValidBusiness(Business business) {
        return (business.getName() != null && business.getDescription() != null && business.getBusinessType() != null);
    }
}
