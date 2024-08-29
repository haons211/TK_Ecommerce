package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserAddressRequest {

    @NotNull
    Integer id;

    @NotBlank
    String name;

    @NotBlank
    @Length(min = 10, max = 12)
    String phoneNumber;

    @NotNull
    Integer wardId;

    @NotBlank
    @Length(max = 500)
    String addressDetail;
}