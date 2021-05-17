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

    Inventory findInventoryByIdAndProductIdAndBusinessId(long id, String productId, long businessId);
    Inventory findInventoryById(long id);

    List<Inventory> findInventoryByProductIdAndBusinessId(String productId, long businessId);

    List<Inventory> findInventoryByBusinessId(long businessId);

}
