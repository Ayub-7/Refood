package org.seng302.repositories;

import org.seng302.models.Product;
import org.seng302.models.Inventory;
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
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Inventory findInventoryByIdAndBusinessId(long id, long businessId);

    Inventory findInventoryByIdAndProductIdAndBusinessId(long id, String productId, long businessId);

    List<Inventory> findInventoryByProductIdAndBusinessId(String productId, long businessId);

    List<Inventory> findInventoryByBusinessId(long businessId);

    Inventory findInventoryById(long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Inventory i SET quantity = :newQuantity where id = :inventoryId")
    void updateInventoryQuantity(@Param(value = "newQuantity") int newQuantity,
                       @Param(value = "inventoryId") long inventoryId);

    //Do when modifying inventory required
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE Inventory p SET id = :newId, name = :name, description = :description, recommended_retail_price = :rrp where id = :prevId")
//    void updateProduct(@Param(value = "newId") String newId,
//                       @Param(value = "name") String name,
//                       @Param(value = "description") String description,
//                       @Param(value = "rrp") Double rrp,
//                       @Param(value = "prevId") String prevId);





}
