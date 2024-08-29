package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.request.DeleleCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.inventory.AddProductToCartRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.service.cartItem.CartItemService;
import com.example.ecommerce.shopbase.service.cartItem.CartItemServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemController {

    CartItemService cartItemService;
    CartItemServiceImpl cartItemServiceImpl;
            @GetMapping("/all")
            public ApiResponse<List<CartItemDetails>> getAllCarItemOnCart(){
                return ApiResponse.<List<CartItemDetails>>builder()
                        .result(cartItemService.getAllCartItemsonCart())
                        .build();

            }
            @PutMapping("/update-items")
           public ApiResponse<Void> updateEntityOfItems(@RequestBody UpdateCartItemRequest updateCartItemRequest){
                cartItemService.updateItems(updateCartItemRequest);
                return ApiResponse.<Void>builder()
                        .message("Success")
                        .build();
            }
            @DeleteMapping("/remove-items")
           public ApiResponse<Void>  removeItemsInCart(@RequestBody DeleleCartItemRequest cartItemRequest ){
                cartItemService.deleteItems(cartItemRequest);
                return ApiResponse.<Void>builder()
                        .message("Success")
                        .build();
            }
            @PostMapping("/add-product-cart")
           public ApiResponse<Void> addProductToCart(@RequestBody AddProductToCartRequest request){
                cartItemService.addProductToCart(request);
                return ApiResponse.<Void>builder()
                        .message("Success")
                        .build();
            }


}
