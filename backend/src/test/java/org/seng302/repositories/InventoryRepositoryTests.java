package org.seng302.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class InventoryRepositoryTests {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private Business business1;

    private Product testProd1;

    private Inventory testInven1;
    private Inventory testInven2;

    private Date dateBefore;
    private Date dateAfter;

    @BeforeEach
    void setUp() {

        Calendar afterCalendar = Calendar.getInstance();
        afterCalendar.set(2022, 1, 1);
        dateAfter = afterCalendar.getTime();

        Calendar beforeCalendar = Calendar.getInstance();
        afterCalendar.set(2020, 1, 1);
        dateBefore = beforeCalendar.getTime();

        //Need to setup business and product for referential integrity
        assertThat(businessRepository).isNotNull();
        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        Business b1 = new Business("Business1", "Test Business 1", a1, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        businessRepository.save(b1);


        assertThat(productRepository).isNotNull();
        testProd1 = new Product("07-4957066", 1, "Spoon", "Soup, Plastic", "Good Manufacturer",  14.69, new Date());
        productRepository.save(testProd1);


        assertThat(inventoryRepository).isNotNull();
        testInven1 = new Inventory("07-4957066", 1, 5, 2.0, 10.0, dateBefore, dateAfter, dateAfter, dateAfter);

        inventoryRepository.save(testInven1);
    }

    @Test
    public void saveInventory() {
        Inventory inventory = new Inventory("07-4957066", 1, 4, 4.0, 16.0, dateBefore, dateAfter, dateAfter, dateAfter);
        inventoryRepository.save(inventory);

        // Test if insertion is properly inserting values.
        Inventory testInventory = inventoryRepository.findInventoryByIdAndProductIdAndBusinessId(2, "07-4957066", 1);
        Assertions.assertEquals(testInventory.getId(), inventory.getId());
        assertThat(testInventory.getBusinessId()).isEqualTo(inventory.getBusinessId());
        assertThat(testInventory.getProductId()).isEqualTo(inventory.getProductId());

        //Test insertion properly works
        List<Inventory> inventoryItems = inventoryRepository.findInventoryByBusinessId(1);
        assertThat(inventoryItems.size()).isEqualTo(2);
        Inventory anotherInventory = new Inventory("07-4957066", 1, 2, 3.0, 6.0, dateBefore, dateAfter, dateAfter, dateAfter);
        inventoryRepository.save(anotherInventory);
        inventoryItems = inventoryRepository.findInventoryByBusinessId(1);
        assertThat(inventoryItems.size()).isEqualTo(3);

        // Inserting the same product id and business id.
        inventoryRepository.save(anotherInventory);
        inventoryItems = inventoryRepository.findInventoryByBusinessId(1);
        assertThat(inventoryItems.size()).isEqualTo(3);
    }
}
