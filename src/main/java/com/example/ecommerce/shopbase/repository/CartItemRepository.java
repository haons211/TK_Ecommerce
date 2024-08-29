package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByUserId(Integer userId);
    Optional<CartItem> findByUserIdAndProduct(int userId, Product product);

    List<CartItem> findByUserIdAndProduct(Integer id, Product product);

    void deleteAllByProductId(Integer productId);

    List<CartItem> findAllByProductId(Integer productId);

}
