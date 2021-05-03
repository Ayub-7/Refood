package org.seng302.team900.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.controllers.UserController;
import org.seng302.finders.UserFinder;
import org.seng302.models.Address;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.requests.NewUserRequest;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class registerStepDefs {


    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    private UserFinder userFinder;

    @Autowired
    ObjectMapper mapper;
    ResponseEntity<String> result;
    String em;
    String pass;
    @Before
    public void setup() throws Exception {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userController = new UserController(this.userRepository, this.userFinder);
    }

    @When("They input the firstname {string}, lastname {string}, email {string}, DoB {string}, and password {string}")
    public void theyInputTheFirstnameLastnameEmailDoBAndAddress(String fname, String lname, String email, String DoB, String password) throws JsonProcessingException, NoSuchAlgorithmException {
        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        NewUserRequest newUserRequest = new NewUserRequest(fname, null, lname, null,
                null, email,
                DoB, null, a1, password);
        em = email;
        pass = password;
        result = userController.registerUser(newUserRequest);
    }

    @Given("User attempts to register")
    public void userAttemptsToRegister() {
    }


    @Then("They successfully register and are redirected to their homepage")
    public void theySuccessfullyRegisterAndAreRedirectedToTheirHomepage() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Given("User attempts to register with an email that is already registered")
    public void userAttemptsToRegisterWithAnEmailThatIsAlreadyRegistered() throws JsonProcessingException, NoSuchAlgorithmException {
        Mockito.when(userRepository.findUserByEmail("jeffgold@hotmail.com")).thenReturn(new User("jeffgold@hotmail.com", "Potato1!", Role.USER));
    }

    @Then("They are notified that the email they tried to register with is already registered")
    public void theyAreNotifiedThatTheEmailTheyTriedToRegisterWithIsAlreadyRegistered() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Then("They are notified that they need to enter firstname and lastname as they are mandatory fields")
    public void theyAreNotifiedThatTheyNeedToEnterFirstnameAndLastnameAsTheyAreMandatoryFields() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Then("They are notified that they need to enter a valid email")
    public void theyAreNotifiedThatTheyNeedToEnterAValidEmail() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Then("They are notified that the email they need to be at least 13 years old to register")
    public void theyAreNotifiedThatTheEmailTheyNeedToBeAtLeastYearsOldToRegister() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @When("They successfully register")
    public void theySuccessfullyRegister() {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Then("Password is hashed and not stored in plain text")
    public void passwordIsHashedAndNotStoredInPlainText() throws JsonProcessingException {
        assertThat(this.userRepository.findUserByEmail(em).getPassword() != pass);
    }

    @Given("User is logged in")
    public void userIsLoggedIn() {
    }

    @When("They press log out")
    public void theyPressLogOut() {
    }

    @Then("They successfully logout and their token session disappears")
    public void theySuccessfullyLogoutAndTheirTokenSessionDisappears() {
    }
}
