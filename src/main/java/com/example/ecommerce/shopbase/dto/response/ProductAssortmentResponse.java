package com.example.ecommerce.shopbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAssortmentResponse {
    String name;
    List<ValueDTO> values;
}
