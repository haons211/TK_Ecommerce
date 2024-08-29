package com.example.ecommerce.shopbase.dto.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CombinationValueRedisDTO {
    private Integer id;

    private Integer quantity;
}
