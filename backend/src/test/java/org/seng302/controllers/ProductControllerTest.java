package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(controllers = ProductController.class)
@ContextConfiguration(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private ProductController productController;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper mapper;

    private User user;
    private Business business;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        user = new User("testemail@email.com", "testpassword", Role.USER);
        user.setId(1L);

        Address businessAddress = new Address(null, null, null, null, "New Zealand", null);
        business = new Business("TestBusiness", "Test Description", businessAddress, BusinessType.RETAIL_TRADE);
        business.setPrimaryAdministrator(user);
        business.setId(1L);

        product1 = new Product("07-4957066", 1, "Spoon", "Soup, Plastic", 14.69, new Date());
        product2 = new Product("07-4957066", 1, "Seedlings", "Buckwheat, Organic", 1.26, new Date());

    }

    @Test
    public void testNoAuthGetBusinessProductCatalogue() throws Exception {
        mvc.perform(get("/businesses/{id}/products", business.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testSuccessfulGetBusinessProductCatalogue() throws Exception {
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(productRepository.findProductsByBusinessId(business.getId())).thenReturn(productList);

        MvcResult result = mvc.perform(get("/businesses/{id}/products", business.getId())
                    .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                    .andReturn();

        // Test for 200 status & correct JSON output.
        assert result.getResponse().getStatus() == HttpStatus.OK.value();
        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(mapper.writeValueAsString(productList));

        // Change to a Global Application Admin user.
        user.setId(99);
        user.setRole(Role.GAA);
        mvc.perform(get("/businesses/{id}/products", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testBadGetBusinessProductCatalogue() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(null);
        mvc.perform(get("/businesses/{id}/products", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());

        User newPrimaryAdmin = new User("email@email.com", "password", Role.USER);
        business.setPrimaryAdministrator(newPrimaryAdmin);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);

        mvc.perform(get("/businesses/{id}/products", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isForbidden());

    }

}
