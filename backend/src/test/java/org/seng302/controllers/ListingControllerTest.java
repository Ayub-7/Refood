package org.seng302.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ca.Cal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.finders.ListingFinder;
import org.seng302.finders.ListingSpecifications;
import org.seng302.models.*;
import org.seng302.models.requests.BusinessListingSearchRequest;
import org.seng302.models.requests.NewBusinessRequest;
import org.seng302.models.requests.NewListingRequest;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.InventoryRepository;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.awt.print.Pageable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(controllers = ListingController.class)
@ContextConfiguration(classes = TestApplication.class)

class ListingControllerTest {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private ListingController listingController;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private InventoryRepository inventoryRepository;
    @MockBean
    private ListingRepository listingRepository;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ListingFinder listingFinder;

    private User ownerUser;
    private User adminUser;
    private User user;
    private Business business;
    private Product product1;
    private Inventory inventory1;
    private Listing listing1;
    private Listing listing2;
    private NewListingRequest newListingRequest;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        ownerUser = new User("Rayna", "YEP", "Dalgety", "Universal", "zero tolerance task-force" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        ownerUser.setId(1L);
        user = new User("Elwood", "YEP", "Altamirano", "Visionary", "mobile capacity", "ealtamirano8@phpbb.com","1927-02-28","+381 643 240 6530",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ItqVNvM2JBA");
        user.setId(2L);
        adminUser = new User("Gannon", "YEP", "Tynemouth", "Exclusive", "6th generation intranet", "gtynemouth1@indiatimes.com","1996-03-31","+62 140 282 1784",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"HGD0nAJNjSD");
        adminUser.setId(3L);

        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        business = new Business("Business1", "Test Business 1", a1, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        business.setId(1L);
        business.createBusiness(ownerUser);
        business.getAdministrators().add(adminUser);

        product1 = new Product("07-4957066", business, "Spoon", "Soup, Plastic", "Good Manufacturer", 14.69, new Date());

        Calendar afterCalendar = Calendar.getInstance();
        afterCalendar.set(2022, 1, 1);
        Date laterDate = afterCalendar.getTime();

        Calendar beforeCalendar = Calendar.getInstance();
        afterCalendar.set(2020, 1, 1);
        Date beforeDate = beforeCalendar.getTime();

        inventory1 = new Inventory("07-4957066", 1, 10, 2.0, 20.0, beforeDate, laterDate, laterDate, laterDate);
        newListingRequest = new NewListingRequest(inventory1.getId(), 2, 2.99, "more info", laterDate);
        listing1 = new Listing(inventory1, 5, 2.0, "Seller may be interested in offers", new Date(), laterDate);
        listingRepository.save(listing1);

        afterCalendar.set(2022, 2, 2);
        Date anotherLaterDate = afterCalendar.getTime();
        listing2 = new Listing(inventory1, 10, 10.0, "Seller may be interested in offers", new Date(), anotherLaterDate);
        listingRepository.save(listing2);

        assertThat(business.getAdministrators().size()).isEqualTo(2);
    }

    //Get listings tests

    @Test
    void testNoAuthGetListing() throws Exception {
        mvc.perform(get("/businesses/{id}/listings", business.getId()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles="USER")
    void testSuccessfulGetBusinessListings() throws Exception {
        List<Listing> listingList = new ArrayList<>();
        listingList.add(listing1);

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(listingRepository.findListingsByInventoryItem(inventory1)).thenReturn(listingList);

        mvc.perform(get("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    void testGetListingForNullBusiness() throws Exception {
        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        User user = new User("New", "User", addr, "email@email.com", "password", Role.USER);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(null);
        mvc.perform(get("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    //
    ////Inventory POST tests
    //

    @Test
    void testNoAuthPostListing() throws Exception {
        mvc.perform(post("/businesses/{id}/listings", business.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    void testForbiddenUserPostListing() throws Exception {
        Address addr = new Address(null, null, null, null, null, "New Zealand", "1234");
        User forbiddenUser = new User("Bad", "User", addr, "email@email.com", "password", Role.USER);

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        //Have as param in here since the request object is null in the test
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, forbiddenUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles="USER")
    void testPostListingAsGlobalAdmin() throws Exception {
        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        User DGAAUser = new User("New", "DGAA", addr, "email@email.com", "password", Role.DGAA);
        User GAAUser = new User("New", "GAA", addr, "email2@email.com", "password", Role.GAA);

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, DGAAUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isCreated());

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, GAAUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(roles="USER")
    void testSuccessfulPostListingAsBusinessAdmin() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isCreated());

        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        User businessSecondaryAdmin = new User("New", "User", addr, "email@email.com", "password", Role.USER);
        business.getAdministrators().add(businessSecondaryAdmin);
        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, businessSecondaryAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="USER")
    void testPostWithNoQuantity() throws Exception {
        listing1.setQuantity(0);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listing1)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostWithQuantityLargerThanInventoryQuantity() throws Exception {
        //inventory quantity is 10
        inventory1.setQuantity(4);
        listing1.setQuantity(8);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listing1)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostWithNegativeQuantity() throws Exception {
        //inventory quantity is 10
        listing1.setQuantity(-2);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listing1)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostWithQuantityEqualToInventoryQuantity() throws Exception {
        //inventory quantity is 10
        inventory1.setQuantity(8);
        newListingRequest.setQuantity(8);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newListingRequest)))
                .andExpect(status().isCreated());

    }


    @Test
    @WithMockUser(roles="USER")
    void testPostWithNegativePrice() throws Exception {
        listing1.setPrice(-2);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listing1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="USER")
    void testPostWithClosingDateInPast() throws Exception {
        Calendar pastDate = Calendar.getInstance();
        pastDate.set(2020, 1, 1);
        Date laterDate = pastDate.getTime();

        listing1.setCloses(laterDate);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductByIdAndBusinessId(null, business.getId())).thenReturn(product1);
        Mockito.when(inventoryRepository.findInventoryById(inventory1.getId())).thenReturn(inventory1);

        mvc.perform(post("/businesses/{id}/listings", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listing1)))
                .andExpect(status().isBadRequest());
    }

    //
    // POST get all listings
    //

    @Test
    void testPostAllListings_noAuth_returnUnauthorized() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content("{}")) // No filter/sort parameters
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostAllListings_invalidSortOption_returnBadRequest() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");

        BusinessListingSearchRequest request = new BusinessListingSearchRequest();
        request.setSortBy("invalidSort");

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content(mapper.writeValueAsString(request))) // No filter/sort parameters
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostAllListings_validRequest_returnOk() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");
        requestParams.add("sortDirection", "asc");

        List<Listing> results = new ArrayList<>();
        results.add(listing1);
        results.add(listing2);

        Mockito.when(listingRepository.findAll()).thenReturn(results);

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content("{}")) // No filter/sort parameters
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostAllListings_sortByProductName_returnOk() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");
        requestParams.add("sortDirection", "asc");

        BusinessListingSearchRequest request = new BusinessListingSearchRequest();
        request.setSortBy("name");

        List<Listing> results = new ArrayList<>();
        results.add(listing1);
        results.add(listing2);

        Mockito.when(listingRepository.findAll()).thenReturn(results);

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostAllListings_sortByExpiry_returnOk() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");
        requestParams.add("sortDirection", "asc");

        BusinessListingSearchRequest request = new BusinessListingSearchRequest();
        request.setSortBy("expires");

        List<Listing> results = new ArrayList<>();
        results.add(listing1);
        results.add(listing2);

        Mockito.when(listingRepository.findAll()).thenReturn(results);

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles="USER")
    void testPostAllListings_sortByCloseDate_returnOk() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");
        requestParams.add("sortDirection", "asc");

        BusinessListingSearchRequest request = new BusinessListingSearchRequest();
        request.setSortBy("closes");

        List<Listing> results = new ArrayList<>();
        results.add(listing1);
        results.add(listing2);

        Mockito.when(listingRepository.findAll()).thenReturn(results);

        mvc.perform(post("/businesses/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

}
