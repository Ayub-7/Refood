package org.seng302.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seng302.models.Listing;
import org.seng302.models.Inventory;
import org.seng302.models.Business;
import org.seng302.repositories.InventoryRepository;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.BusinessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;

@RestController
public class ListingController {
    private static final Logger logger = LogManager.getLogger(ListingController.class.getName());

    @Autowired private BusinessRepository businessRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

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

        List<Listing> listings = new ArrayList<Listing>();

        //Iterate over reach inventory item to get the corresponding listing (if any)
        for (Inventory inventoryItem : inventoryList) {
            List<Listing> listing = listingRepository.findListingsByInventoryItem(inventoryItem);
            listings.addAll(listing);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listings);
    }

}
