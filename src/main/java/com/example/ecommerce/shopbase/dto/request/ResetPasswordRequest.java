package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank
    String token;

    @NotBlank
    @Length(min = 8, message = "Password must have at least 8 characters")
    String newPassword;
}
