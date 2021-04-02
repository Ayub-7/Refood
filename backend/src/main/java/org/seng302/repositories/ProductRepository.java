package org.seng302.repositories;

import org.seng302.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * JPA Repository for the Product entity/table.
 */
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductByIdAndBusinessId(String id, long businessId);

    List<Product> findProductsByBusinessId(long businessId);

}
