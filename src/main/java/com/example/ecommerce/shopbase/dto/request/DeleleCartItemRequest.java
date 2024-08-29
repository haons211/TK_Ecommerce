package com.example.ecommerce.shopbase.dto.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleleCartItemRequest {
    List<CartItemId>  item_ids;


    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemId{
        Integer cart_item_id;
    }
}
