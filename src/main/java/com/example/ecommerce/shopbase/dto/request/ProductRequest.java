package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "Product name not blank")
    String name;


    String description;

    @Min(value = 0, message = "Product price must be greater or equal to 0")
    Integer price;
    @Min(value = 0, message = "Product quantity must be greater or equal to 0")
    Integer quantity;
    @NotNull
    @Min(value = 0, message = "CategoryID must be greater  0")
    Integer categoryId;
    @NotNull
    @Min(value = 0, message = "Weight must be greater  0")
    Integer weight;


}
