package org.seng302.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.UserRepository;
import org.seng302.repositories.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = TestApplication.class)
@WebMvcTest(controllers = WishlistItemController.class)
class WishlistItemControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private WishlistItemController wishlistItemController;
    @MockBean
    private WishlistItemRepository wishlistItemRepository;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private UserRepository userRepository;

    private User user;
    private User otherUser;
    private Business business;
    private WishlistItem wishlistItem;
    private List<WishlistItem> wishlistItemList;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        user = new User("John", "Smith", addr, "johnsmith99@gmail.com", "1337-H%nt3r2", Role.USER);
        otherUser = new User("Other", "User", addr, "otherUser@gmail.com", "1337-H%nt3r2", Role.USER);
        user.setId(1);
        otherUser.setId(2);
        business = new Business("testBusiness", "test description", addr, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        wishlistItem = new WishlistItem(user.getId(), business.getId());

        wishlistItemList = new ArrayList<>();
        wishlistItemList.add(wishlistItem);
    }

    //
    // GET - Listing Notification
    //
    @Test
    void testNoAuthGetWishlistBusinesses() throws Exception {
        mockMvc.perform(get("/users/{id}/wishlist", 1)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetUserWishListBusinesses_invalidUserId_returnNotAcceptable() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(null);
        Mockito.when(wishlistItemRepository.findWishlistItemsByUserId(user.getId())).thenReturn(wishlistItemList);
        mockMvc.perform(get("/users/{id}/wishlist", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testGetUserWishListBusinesses_successfulRetrieval_returnOk() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(wishlistItemRepository.findWishlistItemsByUserId(user.getId())).thenReturn(wishlistItemList);
        mockMvc.perform(get("/users/{id}/wishlist", user.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }


    // POST tests

    @Test
    @WithMockUser
    void testSuccessfulWishlistBusiness() throws Exception {
        Mockito.when(businessRepository.findBusinessById(1)).thenReturn(business);
        mockMvc.perform(post("/businesses/1/wishlist")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isCreated());
    }

    @Test
    void testWishlistBusinessNotLoggedIn() throws Exception {
        mockMvc.perform(post("/businesses/1/wishlist")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testWishlistBusinessNonExistentBusiness() throws Exception {
        Mockito.when(businessRepository.findBusinessById(1)).thenReturn(null);
        mockMvc.perform(post("/businesses/1/wishlist")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void testRemoveWishlistNotLoggedIn() throws Exception {
        mockMvc.perform(delete("/wishlist/1")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testRemoveWishlistSuccessful() throws Exception {
        Mockito.when(wishlistItemRepository.findWishlistItemById(1)).thenReturn(wishlistItem);
        mockMvc.perform(delete("/wishlist/1")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testRemoveWishlistNonExistentWishlistItem() throws Exception {
        Mockito.when(wishlistItemRepository.findWishlistItemById(1)).thenReturn(null);
        mockMvc.perform(delete("/wishlist/1")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testRemoveWishlistWrongUser() throws Exception {
        Mockito.when(wishlistItemRepository.findWishlistItemById(1)).thenReturn(wishlistItem);
        mockMvc.perform(delete("/wishlist/1")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, otherUser))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testRemoveWishlistDGAA() throws Exception {
        otherUser.setRole(Role.DGAA);
        Mockito.when(wishlistItemRepository.findWishlistItemById(1)).thenReturn(wishlistItem);
        mockMvc.perform(delete("/wishlist/1")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, otherUser))
                .andExpect(status().isOk());
    }
}
