package org.seng302.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.controllers.MessageController;
import org.seng302.models.*;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.repositories.MessageRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.bind.ValidationException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessageController.class)
@ContextConfiguration(classes = TestApplication.class)
class MessageControllerTests {
    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private MessageController messageController;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private UserRepository userRepository;


    private User testUserA;
    private User testUserB;



    private List<Message> messages;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException, ValidationException {
        testUserA = new User("Rayna", "YEP", "Dalgety", "Universal", "zero tolerance task-force" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        testUserA.setId(1L);
        testUserB = new User("Elwood", "YEP", "Altamirano", "Visionary", "mobile capacity", "ealtamirano8@phpbb.com","1927-02-28","+381 643 240 6530",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ItqVNvM2JBA");
        testUserB.setId(2L);



        NewCardRequest cardRequest = new NewCardRequest(testUserA.getId(), "Card Title", "Desc", "Test, Two", MarketplaceSection.FORSALE);

        Card card = new Card(cardRequest, testUserA);

        Message message = new Message(testUserB, testUserA, card, "hello", new Date());

        messages = new ArrayList<Message>();
        messages.add(message);
    }


    @Test
    void getMessages_noAuth_returnUnauthorized() throws Exception {
        mvc.perform(get("/users/{userId}/messages", testUserA.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getMessages_badId_returnBadRequest() throws Exception {

        Mockito.when(userRepository.findUserById(testUserA.getId())).thenReturn(testUserA);
        Mockito.when(messageRepository.findMessageByReceiver(testUserA)).thenReturn(null);

        mvc.perform(get("/users/{userId}/messages", 'a'))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getMessages_validIdAndCorrectUser_returnOk() throws Exception {

        Mockito.when(userRepository.findUserById(testUserA.getId())).thenReturn(testUserA);
        Mockito.when(messageRepository.findMessageByReceiver(testUserA)).thenReturn(messages);

        mvc.perform(get("/users/{userId}/messages", 1)
        .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUserA))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getMessages_badUser_returnForbidden() throws Exception {

        Mockito.when(userRepository.findUserById(testUserA.getId())).thenReturn(testUserA);
        Mockito.when(messageRepository.findMessageByReceiver(testUserA)).thenReturn(messages);

        mvc.perform(get("/users/{userId}/messages", 1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUserB))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getMessages_userDoesntExist_returnNotAcceptable() throws Exception {

        Mockito.when(userRepository.findUserById(testUserA.getId())).thenReturn(null);

        mvc.perform(get("/users/{userId}/messages", 1)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, testUserB))
                .andExpect(status().isNotAcceptable());
    }


}