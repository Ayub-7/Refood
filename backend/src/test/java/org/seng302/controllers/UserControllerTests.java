package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    public void setup() {
    }

    @Test
    public void registration() throws Exception {
        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        User user = new User("John", "Hector", "Smith", "Jonny",
                "Likes long walks on the beach", "johnsmith99@gmail.com",
                "1999-04-27", "+64 3 555 0129", a1, "1337-H%nt3r2");

        String userString = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userString)).andExpect(status().is(200));

        mockMvc.perform(post("/users")
                .contentType("application/json")).andExpect(status().is(400));

    }

}
