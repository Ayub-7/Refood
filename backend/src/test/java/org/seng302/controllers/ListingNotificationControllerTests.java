package org.seng302.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.models.requests.ListingNotificationRequest;
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

    private User user;
    private Listing listing;
    private ListingNotification notification;
    private ListingNotificationRequest request;
    private List<ListingNotification> notificationList;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        user = new User("Rayna", "YEP", "Dalgety", "Universal", "" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902", null,"ATQWJM");
        user.setId(1L);
        userRepository.save(user);

        Inventory inventory = new Inventory("07-4957066", 1, 10, 2.0, 20.0, new Date(), new Date(), new Date(), new Date());
        listing = new Listing(inventory, 5, 2.0, "Seller may be interested in offers", new Date(), new Date());
        notification = new ListingNotification(user, listing, NotificationStatus.BOUGHT);

        notificationList = new ArrayList<>();
        notificationList.add(notification);
        assertThat(notificationList.size()).isEqualTo(1);
        request = new ListingNotificationRequest(1, 1, "test", notification.getStatus());
    }

    //
    // POST - Listing Notification
    //

    @Test
    void testPostNewNotification_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/users/{id}/notify", listing.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_noExistingListing_returnUnauthorized() throws Exception {
        Mockito.when(listingRepository.findListingById(1)).thenReturn(null);
        mvc.perform(post("/users/{id}/notify", 1).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testPostNewNotification_successfulNotification_returnCreated() throws Exception {
        ListingNotificationRequest request = new ListingNotificationRequest(1, 1, "test", notification.getStatus());
        Mockito.when(listingRepository.findListingById(1)).thenReturn(listing);
        mvc.perform(post("/users/{id}/notify", listing.getId()).contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
