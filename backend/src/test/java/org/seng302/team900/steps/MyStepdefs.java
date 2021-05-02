package org.seng302.team900.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.seng302.CucumberRunnerTest;
import org.seng302.Main;
import org.seng302.TestApplication;
import org.seng302.controllers.UserController;
import org.seng302.finders.UserFinder;
import org.seng302.models.Address;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.requests.NewUserRequest;
import org.seng302.repositories.UserRepository;
import org.seng302.utilities.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(classes = CucumberRunnerTest.class)
@WebMvcTest(controllers = UserController.class)
public class MyStepdefs {

//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    private UserFinder userFinder;

    @Autowired
    ObjectMapper mapper;

    private static final String DATEFORMAT = "dd/MM/yyyy";
    LoginRequest loginRequest;
    User user;
    ResponseEntity<String> result;

    @Before
    public void setup() throws Exception {
        this.user = new User("johnsmith@yahoo.com", "Potato1!", Role.USER);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userController = new UserController(this.userRepository, this.userFinder);
    }

    @Given("User attempts to login")
    public void userAttemptsToLogin() {
    }

    @When("They enter with email {string} and password {string}")
    public void theyEnterWithEmailAndPassword(String email, String password) throws Exception {
        loginRequest = new LoginRequest(email, password);
        Mockito.when(userRepository.findUserByEmail((loginRequest.getEmail()))).thenReturn(user);
        HttpSession temp = Mockito.mock(HttpSession.class);
        result = userController.loginUser(loginRequest, null, temp);
    }

    @Then("They are redirected to their profile")
    public void theyAreRedirectedToTheirProfile() throws UnsupportedEncodingException {
        assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }


    @When("They enter an email with an invalid format, such that {string}")
    public void theyEnterAnEmailWithAnInvalidFormatSuchThat(String arg0) {
    }

    @Then("They are given a warning that their email format is invalid")
    public void theyAreGivenAWarningThatTheirEmailFormatIsInvalid() {
    }

    @When("They enter an email with the valid format {string} and password {string}")
    public void theyEnterAnEmailWithTheValidFormatAndPassword(String arg0, String arg1) {
    }

    @Then("They are given a warning that either their password or email is incorrect")
    public void theyAreGivenAWarningThatEitherTheirPasswordOrEmailIsIncorrect() {
    }

    @When("They enter only the password {string}")
    public void theyEnterOnlyThePassword(String arg0) {
    }

    @Then("They are given a warning that the email field is empty")
    public void theyAreGivenAWarningThatTheEmailFieldIsEmpty() {
    }

    @When("They enter only the email {string}")
    public void theyEnterOnlyTheEmail(String arg0) {
    }

    @Then("They are given a warning that the password field is empty")
    public void theyAreGivenAWarningThatThePasswordFieldIsEmpty() {
    }

    @When("They enter nothing, but attempt to logging anyway")
    public void theyEnterNothingButAttemptToLoggingAnyway() {
    }

    @Then("They are given a warning that both email and password fields are empty")
    public void theyAreGivenAWarningThatBothEmailAndPasswordFieldsAreEmpty() {
    }

    @Given("User attempts to register")
    public void userAttemptsToRegister() {
    }

    @When("They input the firstname {string}, lastname {string}, email {string}, DoB {string}, address {string}")
    public void theyInputTheFirstnameLastnameEmailDoBAddress(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Then("They successfully register and are redirected to their homepage")
    public void theySuccessfullyRegisterAndAreRedirectedToTheirHomepage() {
    }

    @Given("User attempts to register with an email that is already registered")
    public void userAttemptsToRegisterWithAnEmailThatIsAlreadyRegistered() {
    }

    @Then("They are notified that the email they tried to register with is already registered")
    public void theyAreNotifiedThatTheEmailTheyTriedToRegisterWithIsAlreadyRegistered() {
    }

    @Then("They are notified that they need to enter firstname and lastname as they are mandatory fields")
    public void theyAreNotifiedThatTheyNeedToEnterFirstnameAndLastnameAsTheyAreMandatoryFields() {
    }

    @Then("They are notified that they need to enter a valid email")
    public void theyAreNotifiedThatTheyNeedToEnterAValidEmail() {
    }

    @When("They input the firstname {string}, lastname {string}, email {string}, DoB {string}, and address {string}")
    public void theyInputTheFirstnameLastnameEmailDoBAndAddress(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Then("They are notified that the email they need to be at least {int} years old to register")
    public void theyAreNotifiedThatTheEmailTheyNeedToBeAtLeastYearsOldToRegister(int arg0) {
    }

    @When("They successfully register")
    public void theySuccessfullyRegister() {
    }

    @Then("Password is hashed and not stored in plain text")
    public void passwordIsHashedAndNotStoredInPlainText() {
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
