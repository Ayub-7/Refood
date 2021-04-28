package org.seng302.team900.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class MyStepdefs {
    @Given("I have an account and I want to log in.")
    public void iHaveAnAccountAndIWantToLogIn() {
    }

    @When("I log in as {string} with the correct password.")
    public void iLogInAsWithTheCorrectPassword(String arg0) {
    }

    @Then("I log in successfully.")
    public void iLogInSuccessfully() {
    }

    @Given("I do not have an account.")
    public void iDoNotHaveAnAccount() {
    }

    @When("I register with my full name {string}, aged {int}, using the email {string}, with the password {string}, using the address {string}")
    public void iRegisterWithMyFullNameAgedUsingTheEmailWithThePasswordUsingTheAddress(String arg0, int arg1, String arg2, String arg3, String arg4) {
    }

    @Then("I register successfully and will obtain a token session as a user.")
    public void iRegisterSuccessfullyAndWillObtainATokenSessionAsAUser() {
    }
}
