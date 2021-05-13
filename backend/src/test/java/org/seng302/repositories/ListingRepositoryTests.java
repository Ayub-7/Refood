package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.uti;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests of the user repository.
 */

@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class ListingRepositoryTests {

    @Autowired
    private ListingRepository listingRepository;


    @BeforeEach
    void setUp() {
        assertThat(listingRepository).isNotNull();

        //public Listing(Inventory inventoryItem, int quantity, double price, String moreInfo, Date created, Date closes) {
        //Inventory inventoryItem, int quantity, double price, String moreInfo, Date created, Date closes
        Inventory inventoryItem = new Inventory();
        Date dateCreated = new Date();
        Date dateCloses = new Date();
        dateCloses.setYear(dateCreated.getYear()+1);

        Listing b1 = new Listing(inventoryItem, 12, 12.00, "test more info", dateCreated, dateCloses);

        ListingRepository.save(b1);
    }

    @Test
    public void findListing() {
        Listing found = ListingRepository.findListingById(1);
        assertThat(found.getName()).isEqualTo("Listing1");
        assertThat(found.getListingType()).isEqualTo(ListingType.ACCOMMODATION_AND_FOOD_SERVICES);

        Listing notFound = ListingRepository.findListingById(100);
        assertThat(notFound).isNull();

    }


}
