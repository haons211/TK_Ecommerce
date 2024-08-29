package com.example.ecommerce.shopbase.service.cartItem;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.request.DeleleCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.shopbase.dto.request.inventory.AddProductToCartRequest;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.*;
import com.example.ecommerce.shopbase.mapper.CartItemMapper;

import com.example.ecommerce.shopbase.service.product.ValueCombinationService;
import com.example.ecommerce.shopbase.service.product.ValueService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {


    ValueCombinationService valueCombinationService;
    ValueRepository valueRepository;
    CartItemRepository cartItemRepository;
    ValueCombinationRepository valueCombinationRepository;
    CartItemAttributeRepository cartItemAttributeRepository;
    SecurityUtils securityUtils;
    CartItemMapper cartItemMapper;
    private final ProductRepository productRepository;







 /*public List<CartItemDetails> getAllCartItemsonCart() {
        int userId = securityUtils.getCurrentUserLogin().getId();
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cartItemMapper::toCartItemDetails)
                .collect(Collectors.toList());
    }
    */

    @Override
    public List<CartItemDetails> getAllCartItemsonCart() {
        int userId = securityUtils.getCurrentUserLogin().getId();
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cartItemMapper::toCartItemDetais)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateItems(UpdateCartItemRequest cartItemRequest) {
        List<CartItem> updatedCartItems = new ArrayList<>();
        for (UpdateCartItemRequest.UpdateCartItem item : cartItemRequest.getUpdateItems()) {
            CartItem cartItem = cartItemRepository.findById(item.getCartItem_id())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            int availableQuantity = cartItemAttributeRepository.getValueCombinationByCartItem(cartItem).getQuantity();
            if (item.getQuantity() <= 0) {
                throw new AppException(ErrorCode.QUANTITY_NOT_NEGATIVE);
            }
            cartItem.setQuantity(item.getQuantity());
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    @Override
    public void deleteItems(DeleleCartItemRequest deleteCartItemRequest) {
        List<Integer> cartItemIds = deleteCartItemRequest.getItem_ids().stream()
                .map(DeleleCartItemRequest.CartItemId::getCart_item_id)
                .toList();

        for (Integer cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

            cartItemAttributeRepository.deleteByCartItem(cartItem);
            cartItemRepository.delete(cartItem);
        }
    }


    @Override
    @Transactional
    public void addProductToCart(AddProductToCartRequest values) {
        User currentUser = securityUtils.getCurrentUserLogin();

        Product product;
        Value value1 = null;
        Value value2 = null;

        boolean isSingleValue = values.getValue_id2() == null;
        if (values.getValue_id1() == null && values.getValue_id2() == null) {
            product = productRepository.findById(values.getProduct_id())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        } else {
            value1 = valueRepository.findById(values.getValue_id1())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            if (!isSingleValue) {
                value2 = valueRepository.findById(values.getValue_id2())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
                product = valueCombinationService.findProductByValues(value1, value2);
            } else {
                product = valueCombinationService.findProductByValue(value1);
            }
        }
        List<CartItem> existingCartItems = cartItemRepository.findByUserIdAndProduct(currentUser.getId(), product);

        // Iterate through existing cart items to find a match
        for (CartItem item : existingCartItems) {
            ValueCombination valueCombination = cartItemAttributeRepository.getValueCombinationByCartItem(item);
            boolean valuesMatch = valueCombination != null &&
                    valueCombination.getValue1().getId().equals(values.getValue_id1()) &&
                    (isSingleValue ? valueCombination.getValue2() == null : valueCombination.getValue2().getId().equals(values.getValue_id2()));

            if (valuesMatch) {
                item.setQuantity(item.getQuantity() + 1);
                cartItemRepository.save(item);
                return;  // Match found and item updated, exit the method
            }
        }

        // If no match found, create a new cart item
        CartItem newItem = new CartItem();
        newItem.setUser(currentUser);
        newItem.setProduct(product);
        newItem.setQuantity(1);
        newItem.setSlug(null);

        CartItem savedCartItem = cartItemRepository.save(newItem);

        // Create a new cart item attribute
        if (values.getValue_id1() != null || values.getValue_id2() != null) {
            CartItemAttribute cartItemAttribute = new CartItemAttribute();
            cartItemAttribute.setCartItem(savedCartItem);

            ValueCombination valueCombinationX;
            if (values.getValue_id1() != null && values.getValue_id2() != null) {
                // Case 2: Both value1 and value2 are present
                valueCombinationX = valueCombinationRepository.findFirstByValue1AndValue2(value1, value2)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            } else if (values.getValue_id1() != null) {
                // Case 3: Only value1 is present
                valueCombinationX = valueCombinationRepository.findFirstByValue1(value1)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            } else {
                // Case 1: No value1 or value2
                valueCombinationX = valueCombinationRepository.findFirstByProduct(product)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            }

            cartItemAttribute.setValueCombination(valueCombinationX);

            cartItemAttributeRepository.save(cartItemAttribute);
        }


    }
}







