package com.example.ecommerce.shopbase.service.cartItem;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.request.DeleleCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.inventory.AddProductToCartRequest;


import java.util.List;

public interface CartItemService {

   List<CartItemDetails> getAllCartItemsonCart();
    void updateItems(UpdateCartItemRequest cartItemRequest);
    void deleteItems(DeleleCartItemRequest cartItemRequest);

    void addProductToCart(AddProductToCartRequest request);
}

