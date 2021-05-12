package org.seng302.models.responses;

import lombok.Data;
import org.seng302.models.ProductId;
/**
 * A simple DTO which holds the inventory id to transfer to frontend.
 */
@Data
public class InventoryIdResponse {

    private Long inventoryId;
    private ProductId productId;

    public InventoryIdResponse(Long inventoryId, ProductId productId) {
        this.inventoryId = inventoryId;
        this.productId = productId;
    }

    public InventoryIdResponse(Long inventoryId){
        this.inventoryId = inventoryId;
    }


}
