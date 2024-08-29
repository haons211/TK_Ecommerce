package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.repository.CartItemAttributeRepository;
import com.example.ecommerce.shopbase.service.product.ValueCombinationService;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemMapper {


     CartItemAttributeRepository cartItemAttributeRepository;
     ValueCombinationService valueCombinationService;



    public CartItemDetails toCartItemDetais(CartItem cartItem){
        return CartItemDetails.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .sellerId(cartItem.getProduct().getSeller().getId())
                .name(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .price(getPricePerCartItem(cartItem))
                .weight(getWeightPerCartItem(cartItem))
                .imageUrl(cartItem.getProduct().getImageDefault())
                .valueCombinationDetails(getValueCombinationDetail(cartItem))
                .build();
    }


    public int getPricePerCartItem(CartItem cartItem){
        return  valueCombinationService.getPricePerProduct(cartItem) * cartItem.getQuantity();
    }

    public int getWeightPerCartItem(CartItem cartItem){
        return cartItem.getQuantity() * cartItem.getProduct().getWeight();
    }

    public CartItemDetails.ValueCombinations getValueCombinationDetail(CartItem cartItem){
        ValueCombination valueCombination =cartItemAttributeRepository.getValueCombinationByCartItem(cartItem);
        CartItemDetails.ValueCombinations result = new CartItemDetails.ValueCombinations();
        Value value1= valueCombination.getValue1();
        result.setValue1_name(value1.getValue());
        result.setValue1_id(value1.getId());
        if(valueCombination.getValue2() != null){
            Value value2 = valueCombination.getValue2();
            result.setValue2_name(value2.getValue());
            result.setValue2_id(value2.getId());
        }
        return result;
    }

}
