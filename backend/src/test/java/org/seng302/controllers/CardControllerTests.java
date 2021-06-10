package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.MarketplaceSection;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;

@WebMvcTest(controllers = CardController.class)
@ContextConfiguration(classes = TestApplication.class)
public class CardControllerTests {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private CardController cardController;
    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private ObjectMapper mapper;

    private User testUser;
    private User anotherUser;
    private NewCardRequest cardRequest;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        testUser = new User("Rayna", "YEP", "Dalgety", "", "" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        testUser.setId(1);

        anotherUser = new User("Bob", "", "Loblaw", "", "", "bblaw@email.com", "2006-03-30","+7 684 622 5902", new Address(null, null, null, null, "New Zealand", null), "ATQWJM");
        anotherUser.setId(2);

        cardRequest = new NewCardRequest(testUser.getId(), "Card Title", "Desc", "Test, Two", MarketplaceSection.FORSALE);
    }

    @Test
    public void testPostCard_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(post("/cards")
            .contentType("application/json")
            .content(mapper.writeValueAsString(cardRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testPostCard_wrongCreatorId_returnForbidden() throws Exception {
        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, anotherUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void testPostCard_noKeywords_returnBadRequest() throws Exception {
        cardRequest.setKeywords(null);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testPostCard_noSection_returnBadRequest() throws Exception {
        cardRequest.setSection(null);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testPostCard_returnCreated() throws Exception {
        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testPostCard_asDgaa_returnCreated() throws Exception {
        anotherUser.setRole(Role.DGAA);

        mvc.perform(post("/cards")
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, anotherUser)
                .content(mapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

}
