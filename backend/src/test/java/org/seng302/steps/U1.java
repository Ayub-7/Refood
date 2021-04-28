package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class U1 {

    private SessionFactory sessionFactory;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @Given("I have an account and I want to log in.")
    public void userExists_and_logsIn() {

    }

    @Given("I do not have an account.")
    public void userRegisters() {

    }

    @When("I log in as {string} with the correct password.")
    public void correctLogin() {

    }

    @When("I register with my full name {string}, aged {int}, using the email {string}, with the password {string}, using the address {string}")

    @Then("I log in successfully.")
    public void successLoginSession() {

    }

    @Then("I register successfully and will obtain a token session as a user.")
    public void successRegistration() {

    }
}
