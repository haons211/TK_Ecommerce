package com.example.ecommerce.shopbase.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetails {
    String recipient;
    String messageBody;
    String subject;
}
