package org.seng302.controllers;

import org.seng302.models.Business;
import org.seng302.models.Product;
import org.seng302.models.Role;
import org.seng302.models.User;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * REST controller with product related endpoints.
 */
@RestController
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private BusinessRepository businessRepository;

    /**
     * Retrieves all of the products in the business' product catalogue.
     * Authentication required endpoint - 401 if unauthorized.
     * @param id unique identifier of the business.
     * @return an (potentially empty) array of products that the business owns.
     */
    @GetMapping("/businesses/{id}/products")
    public ResponseEntity<List<Product>> getBusinessProductCatalogue(@PathVariable long id, HttpSession session) {
        Business business = businessRepository.findBusinessById(id);
        if (business == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        User user = (User) session.getAttribute(User.USER_SESSION_ATTRIBUTE);
        if (!business.collectAdministratorIds().contains(user.getId()) && !Role.isGlobalApplicationAdmin(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Product> products = productRepository.findProductsByBusinessId(id);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
