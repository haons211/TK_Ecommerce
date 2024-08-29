package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank
    @Size(min = 8)
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
