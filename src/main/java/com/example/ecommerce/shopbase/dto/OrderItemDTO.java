package com.example.ecommerce.shopbase.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDTO {
    Integer cartItemId;
    Integer quantity;
    Integer price;
}
