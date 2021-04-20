package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

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
import org.seng302.models.requests.NewUserRequest;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.NoSuchAlgorithmException;

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


    User user;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        user = new User("johnsmith99@gmail.com", "1337-H%nt3r2", Role.USER);

    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        NewUserRequest newUserRequest = new NewUserRequest("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newUserRequest)))
                        .andExpect(status().isCreated());

        // With bare minimum requirements.
        Address minAddress = new Address(null, null, null, null, "Canada", null);
        NewUserRequest minUserRequest = new NewUserRequest("John", null, "Smith", null,
                                                        null, "johnsmith99@gmail.com", "1999-04-27", null, minAddress, "1337-H%nt3r2");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(minUserRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    public void testRegistrationWithInvalidAddress() throws Exception {
        NewUserRequest newUserRequest = new NewUserRequest("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", null, "1337-H%nt3r2");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isBadRequest());

        Address address = new Address(null, null, null, null, null, null);
        newUserRequest.setHomeAddress(address);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isBadRequest());

        Address missingCountryAddress = new Address("1","Kropf Court","Jequitinhonha", null, null,"39960-000");
        newUserRequest.setHomeAddress(missingCountryAddress);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testRegistrationWithInvalidUserDetails() throws Exception {
        Address address = new Address(null, null, null, null, "Canada", null);
        NewUserRequest invalidEmailUser = new NewUserRequest("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", null,
                "1999-04-27", "+64 3 555 0129", address, "1337-H%nt3r2");

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(invalidEmailUser)))
                .andExpect(status().isBadRequest());

        invalidEmailUser.setEmail("johnsmith99gmail.com");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(invalidEmailUser)))
                .andExpect(status().isBadRequest());

        NewUserRequest noFirstName = new NewUserRequest(null, null, "Smith", null,
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", null, address, "1337-H%nt3r2");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(noFirstName)))
                .andExpect(status().isBadRequest());

        NewUserRequest noLastName = new NewUserRequest("John", null, null, null,
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", null, address, "1337-H%nt3r2");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(noLastName)))
                .andExpect(status().isBadRequest());

        NewUserRequest noDob = new NewUserRequest("John", null, "Smith", null,
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                null, null, address, "1337-H%nt3r2");
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(noDob)))
                .andExpect(status().isBadRequest());

        NewUserRequest noPassword = new NewUserRequest("John", null, "Smith", null,
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", null, address, null);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(noPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegistrationWithExistingEmail() throws Exception {
        Address minAddress = new Address(null, null, null, null, "Canada", null);
        NewUserRequest minUserRequest = new NewUserRequest("John", null, "Smith", null,
                null, "johnsmith99@gmail.com", "1999-04-27", null, minAddress, "1337-H%nt3r2");

        Mockito.when(userRepository.findUserByEmail(minUserRequest.getEmail())).thenReturn(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(mapper.writeValueAsString(minUserRequest)))
                .andExpect(status().isConflict());

    }

    @Test
    public void testNoAuthGetUser() throws Exception {
        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testSuccessfulGetUser() throws Exception {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/users/{id}", user.getId())).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(user));

    }

    @Test
    @WithMockUser(roles="USER")
    public void testInvalidGetUser() throws Exception {
        Mockito.when(userRepository.findUserById(100)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/users/{id}", user.getId())).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo("");

    }




}
