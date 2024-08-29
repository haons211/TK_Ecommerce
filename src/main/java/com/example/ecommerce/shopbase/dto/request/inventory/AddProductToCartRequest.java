package com.example.ecommerce.shopbase.dto.request.inventory;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductToCartRequest {
    Integer product_id;
    Integer value_id1;
    Integer value_id2;
}
