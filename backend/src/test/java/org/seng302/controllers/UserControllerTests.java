package org.seng302.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.finders.UserFinder;
import org.seng302.models.Address;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.responses.UserIdResponse;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserFinder userFinder;
    @Autowired
    private ObjectMapper mapper;


    @BeforeEach
    public void setup() {
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");


        Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(null);
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andReturn();

        assert result.getResponse().getStatus() == HttpStatus.CREATED.value();
        user.setId(0);
        user.setRole(Role.USER);
        UserIdResponse expectedUserIdResponse = new UserIdResponse(user);
        assertThat(result.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(expectedUserIdResponse));

    }

    @Test
    public void testBadRegistration() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User existingUser = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");

        Mockito.when(userRepository.findUserByEmail(existingUser.getEmail())).thenReturn(existingUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(existingUser)))
                .andExpect(status().isConflict());

        User missingFieldsUser = new User(null, null, null, null, null, "testmail@mail.com", null, null, null, null);
        Mockito.when(userRepository.findUserByEmail(missingFieldsUser.getEmail())).thenReturn(null);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(missingFieldsUser)))
                .andExpect(status().isBadRequest());

        User noEmailUser = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", null,
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        Mockito.when(userRepository.findUserByEmail(noEmailUser.getEmail())).thenReturn(null);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(noEmailUser)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetExistingUser() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        Mockito.when(userRepository.findUserById(0)).thenReturn(user);
        MvcResult userFound = mockMvc.perform(get("/users/{id}",0)
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andReturn();
        assert userFound.getResponse().getStatus() == HttpStatus.OK.value();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGettingNonExistingUser() throws Exception {
        MvcResult userNotFound = mockMvc.perform(get("/users/{id}", 0))
                .andReturn();
        assert userNotFound.getResponse().getStatus() == HttpStatus.NOT_ACCEPTABLE.value();
    }

    @Test
    public void testUnauthorizedGettingUser() throws Exception {
        MvcResult userNotFound = mockMvc.perform(get("/users/{id}", 0))
                .andReturn();
        assert userNotFound.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value();
    }

    @Test
    public void testGoodLogin() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        Mockito.when(userRepository.findUserByEmail("johnsmith99@gmail.com")).thenReturn(user);
        LoginRequest loginReq = new LoginRequest(user.getEmail(), user.getPassword());
        MvcResult success = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginReq)))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.OK.value();
    }

    @Test
    public void testBadPasswordLogin() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        Mockito.when(userRepository.findUserByEmail("johnsmith99@gmail.com")).thenReturn(user);
        LoginRequest loginReq = new LoginRequest(user.getEmail(), "BABABOOEY");
        MvcResult success = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginReq)))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    public void testNonExistingUserLogin() throws Exception {
        LoginRequest loginReq = new LoginRequest("Bazinga", "BABABOOEY");
        MvcResult success = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginReq)))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGoodUserSearch() throws Exception {
        List<User> users = new ArrayList<User>();
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user1 = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        User user2 = new User("Jennifer", "Riley", "Smith", "Jenny",
                "Likes long walks on the beach", "jenthar95@gmail.com",
                "1995-05-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        User user3 = new User("Oliver", "Alfred", "Smith", "Ollie",
                "Likes long walks on the beach", "ollie69@gmail.com",
                "1969-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Mockito.when(userFinder.queryByName("Smith")).thenReturn(users);
        MvcResult results = mockMvc.perform(get("/users/search")
                .param("searchQuery", "Smith"))
                .andReturn();
        assert results.getResponse().getStatus() == HttpStatus.OK.value();

    }

    @Test
    @WithMockUser(roles="DGAA")
    public void testGoodUserAdmin() throws Exception {
        Address a1 = new Address("1", "Kropf Court", "Jequitinhonha", null, "Brazil", "39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");
        Mockito.when(userRepository.findUserById(0)).thenReturn(user);
        MvcResult success =  mockMvc.perform(put("/users/{id}/makeAdmin", 0))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.OK.value();
    }

    @Test
    @WithMockUser(roles="DGAA")
    public void testBadIdUserAdmin() throws Exception {
        MvcResult success =  mockMvc.perform(put("/users/{id}/makeAdmin", 0))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.NOT_ACCEPTABLE.value();
    }

    @Test
    public void testNoTokenUserAdmin() throws Exception {
        MvcResult success =  mockMvc.perform(put("/users/{id}/makeAdmin", 0))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testNoAuthorityUserAdmin() throws Exception {
        MvcResult success =  mockMvc.perform(put("/users/{id}/makeAdmin", 0))
                .andReturn();
        assert success.getResponse().getStatus() == HttpStatus.FORBIDDEN.value();
    }
}
