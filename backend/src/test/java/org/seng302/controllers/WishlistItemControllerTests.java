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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private User user;
    private Business business;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        user = new User("John", "Smith", addr, "johnsmith99@gmail.com", "1337-H%nt3r2", Role.USER);
        business = new Business("testBusiness", "test description", addr, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
    }

    @Test
    @WithMockUser
    void testSuccessfulWishlistBusiness() throws Exception {
        Mockito.when(businessRepository.findBusinessById(1)).thenReturn(business);
        mockMvc.perform(post("/businesses/1/wishlist")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isCreated());
    }
}
