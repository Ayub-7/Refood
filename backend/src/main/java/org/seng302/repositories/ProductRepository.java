package org.seng302.repositories;

import org.seng302.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JPA Repository for the Product entity/table.
 */
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductByIdAndBusinessId(String id, long businessId);

    List<Product> findProductsByBusinessId(long businessId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p SET id = :newId, name = :name, description = :description, recommended_retail_price = :rrp where id = :prevId")
    void updateProduct(@Param(value = "newId") String newId,
                         @Param(value = "name") String name,
                         @Param(value = "description") String description,
                         @Param(value = "rrp") Double rrp,
                         @Param(value = "prevId") String prevId);





}
