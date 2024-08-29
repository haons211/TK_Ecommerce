package com.example.ecommerce.shopbase.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {
    List<UpdateCartItem> updateItems;
    @Data
    public static class UpdateCartItem{
        Integer cartItem_id;
        Integer quantity;
    }
}
