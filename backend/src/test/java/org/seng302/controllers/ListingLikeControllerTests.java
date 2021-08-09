package org.seng302.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.repositories.ListingLikeRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListingLikeController.class)
@ContextConfiguration(classes = TestApplication.class)
public class ListingLikeControllerTests {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private ListingLikeController listingLikeController;
    @MockBean
    private ListingRepository listingRepository;
    @MockBean
    private ListingLikeRepository listingLikeRepository;
    @MockBean
    private UserRepository userRepository;

    private User user;
    private Listing listing;
    private List<ListingLike> likedList;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        user = new User("Rayna", "YEP", "Dalgety", "Universal", "" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902", null,"ATQWJM");
        user.setId(1L);

        Inventory inventory = new Inventory("07-4957066", 1, 10, 2.0, 20.0, new Date(), new Date(), new Date(), new Date());
        listing = new Listing(inventory, 5, 2.0, "Seller may be interested in offers", new Date(), new Date());

        ListingLike like = new ListingLike(user, listing);

        likedList = new ArrayList<>();
        likedList.add(like);
    }

    //
    // POST - Listing Like
    //

    @Test
    void testPostNewLike_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/businesses/listings/{id}/like", listing.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testPostNewLike_noExistingListing_returnUnauthorized() throws Exception {
        Mockito.when(listingRepository.findListingById(1)).thenReturn(null);
        mvc.perform(post("/businesses/listings/{id}/like", 1))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testPostNewLike_successfulLike_returnCreated() throws Exception {
        Mockito.when(listingRepository.findListingById(1)).thenReturn(listing);
        mvc.perform(post("/businesses/listings/{id}/like", 1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isCreated());
    }

    //
    // GET - User liked listings
    //

    @Test
    void testGetUserLikedListings_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(get("/users/{id}/likes", user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetUserLikedListings_noUser_returnBadRequest() throws Exception {
        Mockito.when(userRepository.findUserById(100)).thenReturn(null);

        mvc.perform(get("/users/{id}/likes", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testGetUserLikedListings_notTheUser_returnForbidden() throws Exception {
        User differentUser = new User("First", "Last", null, "email@a.com", "123456", Role.USER);
        differentUser.setId(2);
        Mockito.when(userRepository.findUserById(2)).thenReturn(differentUser);

        mvc.perform(get("/users/{id}/likes", 2)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetUserLikedListings_notTheUserIsDgaa_returnOk() throws Exception {
        User differentUser = new User("First", "Last", null, "email@a.com", "123456", Role.USER);
        differentUser.setId(2);
        user.setRole(Role.DGAA);
        Mockito.when(userRepository.findUserById(2)).thenReturn(differentUser);
        Mockito.when(listingLikeRepository.findListingLikesByUserId(2)).thenReturn(likedList);

        mvc.perform(get("/users/{id}/likes", 2)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetUserLikedListings_isUser_returnOk() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(listingLikeRepository.findListingLikesByUserId(user.getId())).thenReturn(likedList);

        mvc.perform(get("/users/{id}/likes", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }

}
