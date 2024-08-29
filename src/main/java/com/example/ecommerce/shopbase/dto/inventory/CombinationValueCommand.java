package com.example.ecommerce.shopbase.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CombinationValueCommand {

    Integer combinationId;

    Integer changeNumber;

}
