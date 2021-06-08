package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.Card;
import org.seng302.models.MarketplaceSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;


/**
 * Integration tests of the user repository.
 */

@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class CardRepositoryTests {

    @Autowired
    private CardRepository cardRepository;

    private Card testCard1;
    private Card testCard2;
    private String keywords;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        cardRepository.flush();

        assertThat(cardRepository).isNotNull();
        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        Address a2 = new Address("620","Sutherland Lane","Dalai", null,"China", null);
        keywords = "card, test, asdf";

        testCard1 = new Card("test user", a1, "Card title 1", "Card description 1", new Date(), keywords, MarketplaceSection.FORSALE);
        cardRepository.save(testCard1);

        testCard2 = new Card("test user", a1, "Card title 2", "Card description 2", new Date(), keywords, MarketplaceSection.FORSALE);
        cardRepository.save(testCard2);

    }

    /**
     * test findCardById Expects that
     * the previously saved testCards are
     *
     * returned by findCardById(testCards.id)
     */
    @Test
    public void findCardByIdExpectsEquals() {
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
    public void findInventoryByCardTypeExpectsEquals() {
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
    public void findInventoryBySectionExpectsList() {
        List<Card> cardList = cardRepository.findInventoryBySection(MarketplaceSection.FORSALE);
        assertThat(cardList.size()).isEqualTo(2);
    }

    /**
     * test findInventoryBySection Expects that:
     * findInventoryBySection(MarketplaceSection.FORSALE)
     *
     * returns all cards in the marketplace WANTED section (0)
     */
    @Test
    public void findInventoryBySectionExpectsEmptyList() {
        List<Card> cardList = cardRepository.findInventoryBySection(MarketplaceSection.WANTED);
        assertThat(cardList.size()).isEqualTo(0);
    }
}
