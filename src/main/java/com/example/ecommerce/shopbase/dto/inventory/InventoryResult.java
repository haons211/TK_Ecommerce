package com.example.ecommerce.shopbase.dto.inventory;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResult {
    String id;
    Boolean result;
}
