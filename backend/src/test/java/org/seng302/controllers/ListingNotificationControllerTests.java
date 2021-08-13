package org.seng302.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.models.requests.ListingNotificationRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.ListingNotificationRepository;
import org.seng302.repositories.ListingRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListingNotificationController.class)
@ContextConfiguration(classes = TestApplication.class)
public class ListingNotificationControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @InjectMocks
    private ListingNotificationController listingLikeController;
    @MockBean
    private ListingRepository listingRepository;
    @MockBean
    private ListingNotificationRepository listingLikeRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BusinessRepository businessRepository;

    private User user;
    private Business business;
    private Listing listing;
    private ListingNotification notification;
    private ListingNotificationRequest request;
    private List<ListingNotification> notificationList;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        user = new User("Rayna", "YEP", "Dalgety", "Universal", "", "rdalgety3@ocn.ne.jp", "2006-03-30", "+7 684 622 5902", null, "ATQWJM");
        user.setId(1L);
        userRepository.save(user);

        Address address = new Address("39", "Ilam Rd", "Christchurch", "Canterbury", "New Zealand", "8041");

        business = new Business("Howdy", "Partner", address, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        business.setId(1L);
        businessRepository.save(business);

        Inventory inventory = new Inventory("07-4957066", 1, 10, 2.0, 20.0, new Date(), new Date(), new Date(), new Date());
        listing = new Listing(inventory, 5, 2.0, "Seller may be interested in offers", new Date(), new Date());
        listing.setId(1L);
        listingRepository.save(listing);
        notification = new ListingNotification(user, listing, NotificationStatus.BOUGHT);

        notificationList = new ArrayList<>();
        notificationList.add(notification);
        assertThat(notificationList.size()).isEqualTo(1);
        request = new ListingNotificationRequest(1, 1, 1, notification.getStatus(), notification.getCreated());
    }

    //
    // POST - Listing Notification
    //

    @Test
    void testPostNewNotification_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify",
                business.getId(), listing.getId(), user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_noExistingUser_returnNotAcceptable() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(null);
        Mockito.when(listingRepository.findListingById(listing.getId())).thenReturn(listing);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(post("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify",
                business.getId(), listing.getId(), user.getId()).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_noExistingListing_returnNotAcceptable() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(listingRepository.findListingById(listing.getId())).thenReturn(null);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(post("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify",
                business.getId(), listing.getId(), user.getId()).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_noExistingBusiness_returnNotAcceptable() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(listingRepository.findListingById(listing.getId())).thenReturn(listing);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(null);
        mvc.perform(post("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify",
                business.getId(), listing.getId(), user.getId()).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_successfulNotification_returnCreated() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(listingRepository.findListingById(listing.getId())).thenReturn(listing);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(post("/businesses/{businessId}/listings/{listingId}/users/{userId}/notify",
                business.getId(), listing.getId(), user.getId()).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
