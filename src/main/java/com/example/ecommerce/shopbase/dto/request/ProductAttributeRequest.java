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
public class ProductAttributeRequest {


    @NotBlank(message = "Name must be not blank")
    String name;

    String value;
    @NotNull
    @Min(value = 0, message = "ProductId  must be greater or equal to 0")
    Integer productId;
}
