package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.TestApplication;
import org.seng302.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Product testProd1;
    private Product testProd2;

    @BeforeEach
    void setUp() {
        assertThat(productRepository).isNotNull();
        testProd1 = new Product("07-4957066", 1, "Spoon", "Soup, Plastic", "Good Manufacturer",  14.69, new Date());
        testProd2 = new Product("07-4957066", 2, "Seedlings", "Buckwheat, Organic", "Bad Manufacturer", 1.26, new Date());

        productRepository.save(testProd1);
        productRepository.save(testProd2);
    }

    @Test
    void saveProduct() {
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
