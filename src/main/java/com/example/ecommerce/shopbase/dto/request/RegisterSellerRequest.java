package com.example.ecommerce.shopbase.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterSellerRequest {

    @NotBlank
    String name;

    @NotNull
    Integer addressId;

    @NotBlank
    String addressDetail;

    String imagePath;
}
