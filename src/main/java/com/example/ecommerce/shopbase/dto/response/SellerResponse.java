package com.example.ecommerce.shopbase.dto.response;

import com.example.ecommerce.shopbase.dto.PlaceDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SellerResponse {
    Integer sellerId;
    Integer userId;
    String name;

    @JsonIgnore
    String accountName;
    Float rating;
    Integer follow;
    String avatar;
    PlaceDetail place;
    String addressDetail;
}
