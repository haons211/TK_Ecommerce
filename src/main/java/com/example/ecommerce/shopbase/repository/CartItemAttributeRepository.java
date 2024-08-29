package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.CartItemAttribute;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemAttributeRepository extends JpaRepository<CartItemAttribute,Integer> {
    List<CartItemAttribute> findAllByCartItem(CartItem cartItem);

    CartItemAttribute findByCartItem(CartItem cartItem);

    void deleteByCartItem(CartItem cartItem);

    @Query("SELECT ca.valueCombination FROM CartItemAttribute ca WHERE ca.cartItem = :cartItem")
    ValueCombination getValueCombinationByCartItem(@Param("cartItem") CartItem cartItem);

}
