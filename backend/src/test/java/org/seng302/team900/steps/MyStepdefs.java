package org.seng302.team900.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {
    @Given("Registered user attempts to login")
    public void registeredUserAttemptsToLogin() {
    }

    @When("They enter with email {string} and password {string}")
    public void theyEnterWithEmailAndPassword(String arg0, String arg1) {
    }

    @Then("They are redirected to their profile")
    public void theyAreRedirectedToTheirProfile() {
    }

    @Given("User attempts to login")
    public void userAttemptsToLogin() {
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
