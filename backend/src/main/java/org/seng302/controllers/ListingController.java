package org.seng302.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.repositories.*;
import org.seng302.finders.*;

import org.seng302.finders.ListingSpecifications;
import org.seng302.finders.ListingFinder;
import org.seng302.finders.ProductFinder;
import org.seng302.models.*;
import org.seng302.models.requests.BusinessListingSearchRequest;
import org.seng302.models.requests.NewListingRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.xml.bind.ValidationException;
import javax.servlet.http.HttpSession;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Class with endpoints for searching, adding, or deleting listings
 */
@RestController
public class ListingController {
    private static final Logger logger = LogManager.getLogger(ListingController.class.getName());

    @Autowired
    private BusinessRepository businessRepository;


    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ListingLikeRepository listingLikeRepository;

    @Autowired
    private AddressFinder addressFinder;

    @Autowired
    private BoughtListingRepository boughtListingRepository;

    @Autowired
    private ListingNotificationRepository listingNotificationRepository;

    @Autowired
    private ProductFinder productFinder;

    @Autowired
    private ListingFinder listingFinder;

    @Autowired
    private BusinessTypeFinder businessTypeFinder;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Constructor used for cucumber testing.
     * @param listingRepository repository for listings
     */
    public ListingController(ListingRepository listingRepository, ObjectMapper mapper) {
        this.listingRepository = listingRepository;
        this.mapper = mapper;
    }


    /**
     * Get request mapping for getting Listing by id
     * @param id the businesses' id
     * @return ResponseEntity - 401 when unauthorized (handled by spring sec). 406 when Listing doesn't exist. 200 otherwise.
     */
    @GetMapping("/businesses/{id}/listings")
    public ResponseEntity<List<Listing>> getBusinessListings(@PathVariable long id) {
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
     * POST (but actually a GET) endpoint that retrieves listings from all businesses.
     * Body may contain extra search filters and parameters to get specific results.
     * The results are paginated.
     * @param request the search filter/sort information.
     * @param count how many results will show per page.
     * @param offset how many PAGES (not results) to skip before returning the results.
     * @return Response - 400 if body is missing or parameters are invalid, 401 if unauthorized, 200 with paginated results otherwise.
     * @throws JsonProcessingException when writing the results as a string value goes wrong.
     */
    @PostMapping("/businesses/listings")
    public ResponseEntity<String> getAllListings(@RequestBody BusinessListingSearchRequest request,
                                                 @RequestParam("count") int count,
                                                 @RequestParam("offset") int offset,
                                                 @RequestParam("sortDirection") String sortDirection,
                                                 HttpSession session) throws JsonProcessingException {
        Sort sort;
        String sortBy = request.getSortBy();
        if (sortBy == null) {
            sort = Sort.unsorted();
        } else if (sortBy.equalsIgnoreCase("price") ||
                sortBy.equalsIgnoreCase("quantity") ||
                sortBy.equalsIgnoreCase("created") ||
                sortBy.equalsIgnoreCase("closes")) {
                sort = Sort.by(request.getSortBy());
        } else if (sortBy.equalsIgnoreCase("name") || sortBy.equalsIgnoreCase("manufacturer")) {
            sort = Sort.by("inventoryItem.product." + request.getSortBy());
        } else if (sortBy.equalsIgnoreCase("expires")) {
            sort = Sort.by("inventoryItem.expires");
        } else if (sortBy.equalsIgnoreCase("businessType")) {
            sort = Sort.by("inventoryItem.product.business." + request.getSortBy());
        } else if (sortBy.equalsIgnoreCase("city") || sortBy.equalsIgnoreCase("country")) {
            sort = Sort.by("inventoryItem.product.business.address." + request.getSortBy());
        } else if (sortBy.equalsIgnoreCase("seller")) {
            sort = Sort.by("inventoryItem.product.business.name");
        } else { // Sort By parameter is not what we were expecting.
            logger.error(String.format("Unknown sort parameter: %s", request.getSortBy()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected sort parameter.");
        }

        // Sort direction
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        else {
            sort = sort.ascending();
        }

        ListingSpecifications specifications = new ListingSpecifications(request);
        Pageable pageRange = PageRequest.of(offset, count, sort);

        Specification<Listing> specs = where(specifications.hasPriceSet()).and(specifications.hasClosingDateSet());
        if (request.getBusinessQuery() != null && request.getBusinessQuery().length() > 0) {
            specs = specs.and(listingFinder.findListing(request.getBusinessQuery()));
        }
        if (request.getBusinessTypes() != null && !request.getBusinessTypes().isEmpty()) {
            specs = specs.and(businessTypeFinder.findListingByBizType('"' + request.getBusinessTypes().get(0).toString().replace(",", "") + '"'));
            for (int i = 1; i < request.getBusinessTypes().size(); i++) {
                specs = specs.or(businessTypeFinder.findListingByBizType('"' + request.getBusinessTypes().get(i).toString().replace(",", "") + '"'));
            }
        }
        if (request.getProductQuery() != null && request.getProductQuery().length() > 1) { // Prevent product finder from crashing.
            specs = specs.and(productFinder.findProduct(request.getProductQuery()));
        }
        if (request.getAddressQuery() != null && request.getAddressQuery().length() > 0) { // Prevent product finder from crashing.
            specs = specs.and(addressFinder.findAddress(request.getAddressQuery()));
        }
        Page<Listing> result = listingRepository.findAll(specs, pageRange);

        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(result));
    }


    /**
     * Creates a new listing and adds it to the listings of the current acting business
     * Authentication is required, user must be a business admin or a default global admin
     *
     * @param id      unique identifier of the business
     * @param request the request body for the new listing object
     * @param session http session which holds the authenticated user
     * @return error codes: 403 (forbidden user), 400 (bad request for product), 201 (object valid and created)
     */
    @PostMapping("/businesses/{id}/listings")
    public ResponseEntity<String> createListing(@PathVariable long id, @RequestBody NewListingRequest request, HttpSession session) {
        Business business = businessRepository.findBusinessById(id);
        Inventory inventory = inventoryRepository.findInventoryById(request.getInventoryItemId());

        if (inventory == null) { //inventory item doesn't exist for business
            logger.debug(request.getInventoryItemId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
                } catch (ValidationException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }
    }

    /**
     * Delete Listing endpoint, called when a listing is purchased. Deletes notifications and likes related to listing
     * and creates new notification for the boughtListing object. Deletes the inventory item if the listing was the final
     * listing for the item.
     * @param id
     * @return
     */
    @DeleteMapping("/businesses/listings/{id}")
    public ResponseEntity<String> deleteListing(@PathVariable long id) {
        Listing listing = listingRepository.findListingById(id);
        if (listing == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        List<ListingLike> userLikes = listingLikeRepository.findListingLikeByListingId(id);
        Inventory inventory = listing.getInventoryItem();
        for (ListingLike like: userLikes) {
            User user = like.getUser();
            ListingNotification oldNotification = listingNotificationRepository.findListingNotificationsByUserIdAndListing(user.getId(), listing);
            if(oldNotification != null) {
                listingNotificationRepository.delete(oldNotification);
            }
            listingLikeRepository.delete(like);
        }
        listingRepository.delete(listing);
        if (inventory.getQuantity() == 0 && listingRepository.findListingsByInventoryItem(inventory).size() == 1) {
            inventoryRepository.delete(inventory);
            return ResponseEntity.status(HttpStatus.OK).body("Listing and Inventory Item Deleted");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Listing Deleted");
    }
}
