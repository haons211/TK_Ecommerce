package com.example.ecommerce.shopbase.dto.response.inventory;

import com.example.ecommerce.shopbase.entity.OrderItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerOrderItemResponse {
    String buyer_name;
    Integer ord_item_quantity;
    String ord_item_name;
    OrderItem.Status ord_item_status;
    String ord_item_image;
    String ord_item_code;
    Integer ord_item_price;
    String shp_provider;
}
