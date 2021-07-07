package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.repositories.CardRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.bind.ValidationException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

@WebMvcTest(controllers = CardController.class)
@ContextConfiguration(classes = TestApplication.class)
class CardControllerTests {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private CardController cardController;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    private User testUser;
    private User anotherUser;
    private NewCardRequest cardRequest;
    private Card card1;
    private Card card2;
    private Card card;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException, ValidationException {
        testUser = new User("Rayna", "YEP", "Dalgety", "", "" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        testUser.setId(1);
        anotherUser = new User("Bob", "", "Loblaw", "", "", "bblaw@email.com", "2006-03-30","+7 684 622 5902", new Address(null, null, null, null, "New Zealand", null), "ATQWJM");
        anotherUser.setId(2);
        cardRequest = new NewCardRequest(testUser.getId(), "Card Title", "Desc", "Test, Two", MarketplaceSection.FORSALE);

        card = new Card(cardRequest, testUser);

    }

    @Test
    void testPostCard_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/cards")
            .contentType("application/json")
            .content(mapper.writeValueAsString(cardRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testPostCard_wrongCreatorId_returnForbidden() throws Exception {
        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, anotherUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testPostCard_noTitle_returnBadRequest() throws Exception {
        cardRequest.setTitle(null);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testPostCard_shortTitle_returnBadRequest() throws Exception {
        cardRequest.setTitle("A");

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testPostCard_titleTooLong_returnBadRequest() throws Exception {
        cardRequest.setTitle("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testPostCard_noSection_returnBadRequest() throws Exception {
        cardRequest.setSection(null);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testPostCard_returnCreated() throws Exception {
        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testPostCard_asDgaa_returnCreated() throws Exception {
        anotherUser.setRole(Role.DGAA);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, anotherUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetCards_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(get("/cards")
                .param("section", "ForSale"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetCards_noParam_returnBadRequest() throws Exception {
        mvc.perform(get("/cards"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetCards_emptyParam_returnBadRequest() throws Exception {
        mvc.perform(get("/cards")
                .param("section", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetCards_invalidParam_returnBadRequest() throws Exception {
        mvc.perform(get("/cards")
                .param("section", "InvalidSectionName"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetCards_returnOk() throws Exception {
        mvc.perform(get("/cards")
                .param("section", "ForSale"))
                .andExpect(status().isOk());
    }




    //GET by ID tests

    @Test
    void testGetCardById_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(get("/cards/{id}", card.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetCardById_asUser_returnOk() throws Exception {
        Mockito.when(cardRepository.findCardById(card.getId())).thenReturn(card);

        mvc.perform(get("/cards/{id}", card.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetCardById_badId_returnNotAcceptable() throws Exception {
        //If no card found repository will give null
        Mockito.when(cardRepository.findCardById(card.getId())).thenReturn(null);

        mvc.perform(get("/cards/{id}", card.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testGetCardById_idNotLong_returnNotAcceptable() throws Exception {
        //Any value that isn't long will throw 400, just making sure with a float
        mvc.perform(get("/cards/{id}", 1.1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isBadRequest());
    }



    //Get user cards endpoint

    @Test
    void testGetUserCards_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(get("/users/{id}/cards", 1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetUserCards_auth_returnOk() throws Exception {
        int userId = 1;
        Mockito.when(userRepository.findUserById(userId)).thenReturn(testUser);
        mvc.perform(get("/users/{id}/cards", userId)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetUserCards_noUser_returnNotAcceptable() throws Exception {
        int userId = 1;
        Mockito.when(userRepository.findUserById(userId)).thenReturn(null);
        mvc.perform(get("/users/{id}/cards", userId)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testGetUserCards_badId_returnBadRequest() throws Exception {

        mvc.perform(get("/users/{id}/cards", 1.1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser))
                .andExpect(status().isBadRequest());
    }
}
