package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.Product;
import org.seng302.models.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class InventoryRepositoryTests {

    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory testInven1;
    private Inventory testInven2;

    @BeforeEach
    void setUp() {
        assertThat(inventoryRepository).isNotNull();
        testInven1 = new Inventory("07-4957066", 1, 5, 2.0, 10.0, new Date("2021-01-28"), new Date("2021-04-28"), new Date("2021-05-28"), new Date("2021-06-28"))
        testInven2 = new Inventory("55-9986232", 2, 7, 4.0, 28.0, new Date("2021-01-28"), new Date("2021-04-28"), new Date("2021-05-28"), new Date("2021-06-28"))

        inventoryRepository.save(testInven1);
        inventoryRepository.save(testInven2);
    }

    @Test
    public void saveInventory() {
        Product product = new Product("55-9986232", 1, "Lamb Leg", "Bone - In Nz", "Some Manufacturer", 43.66, new Date());
        productRepository.save(product);

        // Test if insertion is properly inserting values.
        Product testProduct = productRepository.findProductByIdAndBusinessId("55-9986232", 1);
        assertThat(testProduct.getId()).isEqualTo(product.getId());
        assertThat(testProduct.getBusinessId()).isEqualTo(product.getBusinessId());
        assertThat(testProduct.getCreated()).isEqualTo(product.getCreated());

        List<Product> products = productRepository.findProductsByBusinessId(1);
        assertThat(products.size()).isEqualTo(2);
        Product anotherProduct = new Product("12-5088639", 1, "Foam Cup", "6 Oz", "Legit Manufacturer", 55.2, new Date());
        productRepository.save(anotherProduct);

        products = productRepository.findProductsByBusinessId(1);
        assertThat(products.size()).isEqualTo(3);

        // Inserting the same product id and business id.
        productRepository.save(anotherProduct);
        products = productRepository.findProductsByBusinessId(1);
        assertThat(products.size()).isEqualTo(3);
    }
}
