package com.example.ecommerce.shopbase.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String name;
    String description;
    Integer price;
    Integer quantity;
    Integer weight;
    Integer categoryId;
    String imageUrl;
    Integer sold;
}