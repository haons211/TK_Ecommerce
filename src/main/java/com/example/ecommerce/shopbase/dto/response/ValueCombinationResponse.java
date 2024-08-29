package com.example.ecommerce.shopbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValueCombinationResponse {
    Integer id;
    String value1;
    String value2;
    Integer productId;
    Integer price;
    Integer quantity;
}
