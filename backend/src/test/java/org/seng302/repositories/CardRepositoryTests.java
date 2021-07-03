package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.Card;
import org.seng302.models.User;
import org.seng302.models.requests.NewCardRequest;

import org.seng302.models.MarketplaceSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import javax.xml.bind.ValidationException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


/**
 * Integration tests of the user repository.
 */

@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
class CardRepositoryTests {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;


    private Card testCard1;
    private Card testCard2;
    private String keywords;
    private User testUser;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, ValidationException {
        cardRepository.deleteAll();
        cardRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();

        assertThat(userRepository).isNotNull();
        assertThat(cardRepository).isNotNull();

        Address a1 = new Address(null, null, null, null, "New Zealand", null);
        Address a2 = new Address(null, null, null, null, "Australia", null);
        User testUser = new User("Wileen", "YEP", "Tilsley","Diverse", "hybrid orchestration","wtilsley0@rakuten.co.jp","1921-10-08","+86 815 603 3959",a1, "zWkb3AeLn3lc");
        User user2 = new User("Gannon", "YEP", "Tynemouth", "Exclusive", "6th generation intranet", "gtynemouth1@indiatimes.com","1996-03-31","+62 140 282 1784",a2,"HGD0nAJNjSD");
        userRepository.save(testUser);
        userRepository.save(user2);

        keywords = "card, test, asdf";

        NewCardRequest newCardRequest1 = new NewCardRequest(1, "Card title 1", "Card description 1", keywords, MarketplaceSection.FORSALE);
        NewCardRequest newCardRequest2 = new NewCardRequest(2, "Card title 2", "Card description 2", keywords, MarketplaceSection.FORSALE);

        testCard1 = new Card(newCardRequest1, testUser);
        cardRepository.save(testCard1);

        testCard2 = new Card(newCardRequest2, user2);
        cardRepository.save(testCard2);

    }

    /**
     * test findCardById Expects that
     * the previously saved testCards are
     *
     * returned by findCardById(testCards.id)
     */
    @Test
    void findCardByIdExpectsEquals() {
        Card card1 = cardRepository.findCardById(testCard1.getId());
        assertThat(card1).isEqualTo(testCard1);

        Card card2 = cardRepository.findCardById(testCard2.getId());
        assertThat(card2).isEqualTo(testCard2);
    }

    /**
     * test findCardsByKeywords Expects that:
     * findCardsByKeywords(testCards.getKeywords)
     *
     * returns all card with matching keywords (2)
     */
    @Test
    void findInventoryByCardTypeExpectsEquals() {
        List<Card> cardList = cardRepository.findCardsByKeywords(keywords);
        assertThat(cardList.size()).isEqualTo(2);

    }

    /**
     * test findInventoryBySection Expects that:
     * findInventoryBySection(MarketplaceSection.FORSALE)
     *
     * returns all cards in the marketplace FORSALE section (2)
     */
    @Test
    void findInventoryBySectionExpectsList() {
        List<Card> cardList = cardRepository.findAllBySection(MarketplaceSection.FORSALE);
        assertThat(cardList.size()).isEqualTo(2);
    }

    /**
     * test findInventoryBySection Expects that:
     * findInventoryBySection(MarketplaceSection.FORSALE)
     *
     * returns all cards in the marketplace WANTED section (0)
     */
    @Test
    void findInventoryBySectionExpectsEmptyList() {
        List<Card> cardList = cardRepository.findAllBySection(MarketplaceSection.WANTED);
        assertThat(cardList.size()).isEqualTo(0);
    }
}
