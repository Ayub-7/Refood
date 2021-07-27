package org.seng302.team900.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.seng302.controllers.CardController;
import org.seng302.controllers.UserController;
import org.seng302.finders.UserFinder;
import org.seng302.models.Address;
import org.seng302.models.Card;
import org.seng302.models.MarketplaceSection;
import org.seng302.models.User;
import org.seng302.models.requests.LoginRequest;
import org.seng302.models.requests.NewCardRequest;
import org.seng302.repositories.CardRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static java.time.Instant.ofEpochMilli;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class cardExpiryDefs {
    private User testUser;
    private NewCardRequest cardRequest;
    private Card card;
    private Address addr;
    private Date firstDate;
    private Date secondDate;

    @Given("An existing card has just expired")
    public void anExistingCardHasJustExpired() throws NoSuchAlgorithmException, ValidationException {
        addr = new Address(null, null, null, null, null, "Australia", "12345");
        testUser = new User("Rayna", "YEP", "Dalgety", "", "" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        testUser.setId(1);
        cardRequest = new NewCardRequest(testUser.getId(), "Card Title", "Desc", "Test, Two", MarketplaceSection.FORSALE);
        card = new Card(cardRequest, testUser);
        card.setDisplayPeriodEnd(new Date());
    }

    @When("I choose to extend it")
    public void iChooseToExtendIt() {
        firstDate = card.getDisplayPeriodEnd();
        card.updateDisplayPeriodEndDate();
        secondDate = card.getDisplayPeriodEnd();
    }

    @Then("The expiry date for the card is extended and the card is still there")
    public void theExpiryDateForTheCardIsExtendedAndTheCardIsStillThere() {
        assertThat(firstDate).isNotEqualTo(secondDate);
    }
}
