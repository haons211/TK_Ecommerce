package com.example.ecommerce.shopbase.dto.request;

import com.example.ecommerce.shopbase.dto.product.ValueCombinationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AssortmentValueCombinationRequest {
    Integer productId;
    List<AssortmentRequest> assortmentList;
    List<ValueCombinationDTO> valueCombination;

}
