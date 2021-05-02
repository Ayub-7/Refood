package org.seng302.team900.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.seng302.controllers.UserController;
import org.seng302.finders.UserFinder;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

public class registerStepDefs {


    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    private UserFinder userFinder;

    @Autowired
    ObjectMapper mapper;

    LoginRequest loginRequest;
    User user;
    ResponseEntity<String> result;


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
