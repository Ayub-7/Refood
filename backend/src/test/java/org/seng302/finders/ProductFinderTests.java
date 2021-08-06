package org.seng302.finders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.seng302.Main;
import org.seng302.MainApplicationRunner;
import org.seng302.TestApplication;
import org.seng302.controllers.ProductController;
import org.seng302.models.Product;
import org.seng302.repositories.ProductRepository;
import org.seng302.repositories.UserRepository;

import org.seng302.utilities.AdminUtils;
import org.seng302.utilities.SchedAdminCheck;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.List;



@SpringBootTest(classes = Main.class)
@ContextConfiguration(classes = TestApplication.class)
public class ProductFinderTests {

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private SchedAdminCheck schedAdminCheck;

    @MockBean
    private AdminUtils adminUtils;

    @MockBean
    private ProductController productController;

    @Mock
    private Product product;

    @Autowired
    private ProductFinder productFinder;



    private Product product1;
    private Product product2;

    @BeforeEach
    void setup() throws Exception {

        product1 = new Product("07-4957067", 1, "Chinese Food", "Soup, Plastic", "Good Manufacturer", 14.69, new Date());
        product2 = new Product("07-4957066", 1, "Thai Food", "Buckwheat, Organic", "Bad Manufacturer", 1.26, new Date());

        productRepository.save(product1);
        productRepository.save(product2);
    }


    @Test
    void testProductFindSingleNameReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese Food");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertEquals(products.size(), 1);
    }

    @Test
    void testProductFindOrReturnsTwoProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese OR Thai");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertTrue(products.contains(product1) && products.contains(product2));
        Assertions.assertEquals(products.size(), 2);
    }

    @Test
    void testProductFindAndReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese AND Food");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertEquals(products.size(), 1);
    }

    @Test
    void testProductFindWordWithSpacesReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("Chinese Food");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertEquals(products.size(), 1);
    }

    @Test
    void testProductFindBadSearchReturnsNone() throws Exception {
        Specification<Product> spec = productFinder.findProduct("djsakldjsakl");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertEquals(products.size(), 0);
    }

    @Test
    void testProductFindWithQuotesReturnsOneProduct() throws Exception {
        Specification<Product> spec = productFinder.findProduct("\"Chinese Food\"");
        List<Product> products = productRepository.findAll(spec);
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertEquals(products.size(), 1);
    }

    @Test
    void testProductFindWithQuotesNotMatchReturnsNone() throws Exception {
        Specification<Product> spec = productFinder.findProduct("\"Chinese Foo\"");
        List<Product> products = productRepository.findAll(spec);
        System.out.println(products);
        Assertions.assertEquals(products.size(), 0);
    }

}
