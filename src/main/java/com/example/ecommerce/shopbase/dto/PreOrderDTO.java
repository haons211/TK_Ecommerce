package com.example.ecommerce.shopbase.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreOrderDTO {
    Integer userId;
    String status;
    List<OrderItemDTO> orderItems;
}
