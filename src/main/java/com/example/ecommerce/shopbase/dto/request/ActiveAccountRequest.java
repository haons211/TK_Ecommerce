package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActiveAccountRequest {

    final String emailRegexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


    @NotBlank
    @Length(min = 4, max = 4, message = "OTP must have 4 characters")
    String otp;

    @NotBlank
    @Email(regexp = emailRegexp, message = "Invalid email pattern")
    String email;
}
