package com.example.ecommerce.shopbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressResponse {
    Integer id;
    Integer userId;
    String city;
    String district;
    String ward;
    String addressDetail;
    Boolean isDefault;
}
