package com.example.ecommerce.shopbase.dto.product;

import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductValueCombination {
    Product product;
    List<ValueCombination> valueCombinations;
}
