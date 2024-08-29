package com.example.ecommerce.shopbase.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemDetails {
    int id;
    int productId;
    int sellerId;
    String name;
    int quantity;
    int price;
    int weight;
    String imageUrl;

    ValueCombinations valueCombinationDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ValueCombinations {
        Integer value1_id;
        Integer value2_id;
       String  value1_name;
       String  value2_name;
    }
}
