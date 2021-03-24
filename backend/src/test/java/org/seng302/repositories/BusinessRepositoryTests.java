package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.Business;
import org.seng302.models.BusinessType;
import org.seng302.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests of the user repository.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class BusinessRepositoryTests {

    @Autowired
    private BusinessRepository businessRepository;


    private Business business;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        assertThat(businessRepository).isNotNull();
        Business b1 = new Business("Business1", "Test Business 1", "123 Test Street", BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        Business b2 = new Business("Business2", "Test Business 2", "23 Testing Avenue", BusinessType.RETAIL_TRADE);
        businessRepository.save(b1);
        businessRepository.save(b2);
    }

    @Test
    public void findBusiness() {
        Business found = businessRepository.findBusinessById(1);
        assertThat(found.getName()).isEqualTo("Business1");
        assertThat(found.getBusinessType()).isEqualTo(BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);

        Business notFound = businessRepository.findBusinessById(100);
        assertThat(notFound).isNull();

    }


}
