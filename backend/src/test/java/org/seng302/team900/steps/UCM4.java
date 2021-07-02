package org.seng302.team900.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.controllers.CardController;
import org.seng302.repositories.CardRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebMvcTest(controllers = CardController.class)
public class UCM4 {

    @InjectMocks
    private CardController cardController;

    @MockBean
    private CardRepository cardRepository;

    @Given("An existing card has just expired")
    public void anExistingCardHasJustExpired() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("NZST"));
        Date d = new Date(); //current date
        Date dateBefore = new Date(d.getTime() - 14 * 24 * 3600 * 1000);
        Date NOW = sdf.parse(dateBefore.toString());
        Mockito.when(Date.class).thenReturn(NOW);
    }

    @When("I choose to extend it")
    public void iChooseToExtendIt() {
    }

    @Then("The expiry date for the card is extended and the card is still there")
    public void theExpiryDateForTheCardIsExtendedAndTheCardIsStillThere() {
    }
}
