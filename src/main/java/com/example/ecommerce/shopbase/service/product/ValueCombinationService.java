package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.ValueCombinationRequest;

import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;

import java.util.List;

public interface ValueCombinationService {
    int getPricesByValueIdsWithBothValues(ValueCombinationRequest valueCombination);
    void deleteValueCombination(Integer valueCombinationId);
    List<ValueCombination> getAllValueCombinationsbyProductId(Integer productId);
     /*Integer getProductIdByValueId(int valueId1);*/
   /* Product findProductByValueId(int valueId1);*/
    Product findProductByValue(Value value);
    Product findProductByValues(Value value1, Value value2);
    int getPricePerProduct(CartItem cartItem);
    int getQuantityPerProduct(CartItem cartItem);
}
