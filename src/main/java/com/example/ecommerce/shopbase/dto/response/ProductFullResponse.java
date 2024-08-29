package com.example.ecommerce.shopbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFullResponse {
    ProductResponse productResponse;
    List<ProductAttributeResponse> productAttributeResponse;
    List<ProductAssetResponse> productAssetResponse;
    List<ValueCombinationResponse> valueCombinationResponse;
}
