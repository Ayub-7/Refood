package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@EnableAutoConfiguration
@AutoConfigureMockMvc()
@ContextConfiguration(classes = UserController.class)
@WebMvcTest
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    @AutoConfigureMockMvc(addFilters = false)

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registration() throws Exception {
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", "4 Rountree Street, Upper Riccarton", "1337-H%nt3r2");

        String userString = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userString)).andExpect(status().is(200));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userString)).andExpect(status().is(409));

        mockMvc.perform(post("/users")
                .contentType("application/json")).andExpect(status().is(400));

    }

}
