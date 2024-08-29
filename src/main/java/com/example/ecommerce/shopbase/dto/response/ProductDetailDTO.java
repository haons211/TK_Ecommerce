package com.example.ecommerce.shopbase.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailDTO {
    Integer productId;
    String productName;
    String description;
    Integer price;
    Integer quantity;
    Integer weight;
    Integer categoryId;
    String imageUrl;
    Integer sold;
    List<String> assets;
    List<ProductAttributeResponse> attributes;
    List<ProductAssortmentResponse> assortments;
    List<ValueCombinationResponse> options;
}
