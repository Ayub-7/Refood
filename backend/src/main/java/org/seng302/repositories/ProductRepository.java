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
    @Query("UPDATE Product p SET id = :newId where id = :prevId")
    void updateProductId(@Param(value = "newId") String newId, @Param(value = "prevId") String prevId);
    // @Query("UPDATE Product p SET name = :name where id = :id")
    // void updateProductName(@Param(value = "name") String name, @Param(value = "id") String id);
    // @Query("UPDATE Product p SET description = :description where id = :id")
    // void updateProductDescription(@Param(value = "description") String description, @Param(value = "id") String id);
    // @Query("UPDATE Product p SET recommended_retail_price = :rrp where id = :id")
    // void updateProductRRP(@Param(value = "rrp") double rrp, @Param(value = "id") String id);





}
