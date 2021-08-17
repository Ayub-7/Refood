package org.seng302.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.models.requests.ListingNotificationRequest;
import org.seng302.repositories.*;
import org.seng302.controllers.ListingLikeController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private ListingLikeRepository listingLikeRepository;
    @MockBean
    private ListingNotificationRepository listingNotificationRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private BoughtListingRepository boughtListingRepository;

    private User user;
    private Business business;
    private Listing listing;
    private Listing listing2;
    private BoughtListing boughtListing1;
    private BoughtListing boughtListing2;
    private ListingNotification notification;
    private ListingNotification notification2;
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
        Inventory inventory2 = new Inventory("12-3456678", 1, 10, 5.0, 50.0, new Date(), new Date(), new Date(), new Date());
        listing = new Listing(inventory, 5, 2.0, "Seller may be interested in offers", new Date(), new Date());
        listing.setId(1L);
        listing2 = new Listing(inventory2, 5, 5.0, "Test more info", new Date(), new Date());
        listing2.setId(2L);
        boughtListing1 = new BoughtListing(user, listing);
        boughtListing2 = new BoughtListing(user, listing2);
        boughtListing1.setId(1L);
        boughtListing2.setId(2L);
        boughtListingRepository.save(boughtListing1);
        boughtListingRepository.save(boughtListing2);
        notification = new ListingNotification(user, boughtListing1, NotificationStatus.BOUGHT);
        notification2 = new ListingNotification(user, boughtListing2, NotificationStatus.BOUGHT);

        notificationList = new ArrayList<>();
        notificationList.add(notification);
        notificationList.add(notification2);
        assertThat(notificationList.size()).isEqualTo(2);
    }

    //
    // POST - Listing Notification
    //

    /**
     * Tests that a user not logged in can't make the POST
     */
    @Test
    void testPostNewNotification_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/listings/{listingId}/notify", listing.getId()))
                .andExpect(status().isUnauthorized());
    }

    /**
     * An invalid listing returns 406 (not acceptable)
     */
    @Test
    @WithMockUser
    void testPostNewNotification_noExistingListing_returnNotAcceptable() throws Exception {
        Mockito.when(boughtListingRepository.findBoughtListingByListingId(listing.getId())).thenReturn(null);
        mvc.perform(post("/listings/{listingId}/notify", listing.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Works as expected with valid user, listing and valid IDs
     */
    @Test
    @WithMockUser
    void testPostNewNotification_successfulNotification_returnCreated() throws Exception {
        Mockito.when(boughtListingRepository.findBoughtListingByListingId(listing.getId())).thenReturn(boughtListing1);
        mvc.perform(post("/listings/{listingId}/notify", listing.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isCreated());
    }

    //
    // GET - Listing Notification
    //

    /**
     * Tests not logged in user, should return Unauthorized
     * @throws Exception
     */
    @Test
    void testNoAuthGetListingNotifications() throws Exception {
        mvc.perform(get("/users/{userId}/notifications", user.getId())).andExpect(status().isUnauthorized());
    }

    /**
     * Tests unsuccessful retrieval of all listing notifications due to invalid userId
     * @throws Exception
     */
    @Test
    @WithMockUser
    void testGetNotifications_invalidUserId_returnNotAcceptable() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(null);
        Mockito.when(listingNotificationRepository.findListingNotificationsByUserId(user.getId())).thenReturn(null);
        mvc.perform(get("/users/{userId}/notifications", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Tests successful retrieval of all listing notifications
     * @throws Exception
     */
    @Test
    @WithMockUser
    void testGetNotifications_successfulRetrieval_returnOk() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(listingNotificationRepository.findListingNotificationsByUserId(user.getId())).thenReturn(null);
        mvc.perform(get("/users/{userId}/notifications", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }
}
