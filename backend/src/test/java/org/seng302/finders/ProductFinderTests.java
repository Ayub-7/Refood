package org.seng302.finders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.ProductRepository;

import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;


import java.util.Date;
import java.util.List;



@SpringBootTest(classes = Main.class)
@ContextConfiguration(classes = TestApplication.class)
public class ProductFinderTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductFinder productFinder;


    private Product product1;
    private Product product2;

    private User user;
    private Business business;

    @BeforeEach
    void setup() throws Exception {

        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        user = new User("New", "User", addr, "testemail@email.com", "testpassword", Role.USER);
        user.setId(1L);

        userRepository.save(user);

        Address businessAddress = new Address(null, null, null, null, "New Zealand", null);
        business = new Business("TestBusiness", "Test Description", businessAddress, BusinessType.RETAIL_TRADE);
        business.createBusiness(user);
        business.setId(1L);

        businessRepository.save(business);


        product1 = new Product("07-4957067", business, "Chinese Food", "Soup, Plastic", "Good Manufacturer", 14.69, new Date());
        product2 = new Product("07-4957066", business, "Thai Food", "Buckwheat, Organic", "Bad Manufacturer", 1.26, new Date());

        productRepository.save(product1);
        productRepository.save(product2);
    }


    @Test
    void testProductFindSingleNameReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese Food");
        List<Product> products = productRepository.findAll(spec);
        boolean contains = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(contains);
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void testProductFindOrReturnsTwoProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese OR Thai");
        List<Product> products = productRepository.findAll(spec);
        boolean containsProduct1 = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        boolean containsProduct2 = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(containsProduct1 && containsProduct2);
        Assertions.assertEquals(2, products.size());
    }

    @Test
    void testProductFindAndReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese AND Food");
        List<Product> products = productRepository.findAll(spec);
        boolean contains = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(contains);
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void testProductFindWordWithSpacesReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese Food");
        List<Product> products = productRepository.findAll(spec);
        boolean contains = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(contains);
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void testProductFindBadSearchReturnsNone() throws Exception {
        Specification<Product> spec = productFinder.findProduct("djsakldjsakl");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertEquals(0, products.size());
    }

    @Test
    void testProductFindWithQuotesReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("\"Chinese Food\"");
        List<Product> products = productRepository.findAll(spec);
        boolean contains = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(contains);
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void testProductFindWithQuotesNotMatchReturnsResult() throws Exception {
        Specification<Product> spec = productFinder.findProduct("\"Chinese Foo\"");
        List<Product> products = productRepository.findAll(spec);
        boolean contains = products.stream().anyMatch(o -> o.getName().equals(product1.getName()));
        Assertions.assertTrue(contains);
        Assertions.assertEquals(1, products.size());
    }

}
