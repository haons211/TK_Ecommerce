package com.example.ecommerce.shopbase.service.cartItem;

import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.repository.CartItemAttributeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemAttributeServiceImpl implements CartItemAttributeService {


    CartItemAttributeRepository cartItemAttributeRepository;



    @Override
    public void deleteByCartItem(CartItem cartItem) {


    }
}
