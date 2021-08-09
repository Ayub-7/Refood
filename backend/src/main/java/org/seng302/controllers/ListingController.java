package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.seng302.models.*;
import org.seng302.models.requests.NewListingRequest;
import org.seng302.repositories.InventoryRepository;
import org.seng302.finders.AddressFinder;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.BusinessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;


import javax.xml.bind.ValidationException;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class ListingController {
    private static final Logger logger = LogManager.getLogger(ListingController.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired private BusinessRepository businessRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private AddressFinder addressFinder;

    /**
     * Get request mapping for getting Listing by id
     * @param id the businesses' id
     * @return ResponseEntity - 401 when unauthorized (handled by spring sec). 406 when Listing doesn't exist. 200 otherwise.
     * @throws JsonProcessingException when json mapping object to a json string fails unexpectedly.
     */
    @GetMapping("/businesses/{id}/listings")
    public ResponseEntity<List<Listing>> getListings(@PathVariable long id) throws JsonProcessingException {
        Business business = businessRepository.findBusinessById(id);
        if (business == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        List<Inventory> inventoryList = inventoryRepository.findInventoryByBusinessId(id);

        List<Listing> listings = new ArrayList<>();

        //Iterate over reach inventory item to get the corresponding listing (if any)
        for (Inventory inventoryItem : inventoryList) {
            List<Listing> listing = listingRepository.findListingsByInventoryItem(inventoryItem);
            listings.addAll(listing);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listings);
    }


    /**
     * Creates a new product and adds it to the product catalogue of the current acting business
     * Authentication is required, user must be a business admin or a default global admin
     * @param id unique identifier of the business
     * @param request the request body for the new listing object
     * @param session http session which holds the authenticated user
     * @return error codes: 403 (forbidden user), 400 (bad request for product), 201 (object valid and created)
     * @throws JsonProcessingException
     */
    @PostMapping("/businesses/{id}/listings")
    public ResponseEntity<String> createListing(@PathVariable long id, @RequestBody NewListingRequest request, HttpSession session) {
        Business business = businessRepository.findBusinessById(id);
        Inventory inventory = inventoryRepository.findInventoryById(request.getInventoryItemId());

        if(inventory == null){ //inventory item doesen't exist for business
            logger.debug(request.getInventoryItemId());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (business == null) { // Business does not exist
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } else {
            ArrayList<Long> adminIds = business.getAdministrators().stream().map(User::getId).collect(Collectors.toCollection(ArrayList::new));
            User currentUser = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);

            if (!(adminIds.contains(currentUser.getId()) || Role.isGlobalApplicationAdmin(currentUser.getRole()))) { // User is not authorized to add products
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else { // User is authorized
                try {
                    Listing newListing = new Listing(inventory, request);
                    inventoryRepository.updateInventoryQuantity(inventory.getQuantity() - request.getQuantity(), request.getInventoryItemId());
                    listingRepository.save(newListing);
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                } catch (ValidationException e){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }
    }

    @GetMapping("/businesses/location")
            public ResponseEntity<String> findBusinesses(@RequestParam(name="query") String query, HttpSession session) throws JsonProcessingException {
                logger.debug("Searching for businesses...");
                System.out.println("Searching for businesses... address");
                Specification<Listing> specification = addressFinder.findAddress(query);
                System.out.println(specification);
                List<Listing> businesses = listingRepository.findAll(specification);

                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(businesses));
            }

}
