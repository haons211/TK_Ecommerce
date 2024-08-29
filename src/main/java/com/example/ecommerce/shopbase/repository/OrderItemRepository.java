package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

  @Query("SELECT oi FROM OrderItem oi WHERE oi.seller.id = :sellerId")
  List<OrderItem> findBySeller(@Param("sellerId") int sellerId);

//  @Modifying
//  @Transactional
//  @Query("UPDATE OrderItem oi SET oi.cartItem.id = 99999999 WHERE oi.cartItem.id = :cartItemId")
//  void updateCartItemIdToZero(@Param("cartItemId") int cartItemId);

  @Query("SELECT oi FROM OrderItem oi WHERE oi.cartItem.id = :cartItemId")
  List<OrderItem> findAllByCartItemId(@Param("cartItemId") int cartItemId);


  @Modifying
  @Transactional
  @Query("DELETE FROM OrderItem oi WHERE oi.cartItem.id = :cartItemId")
  void deleteAllByCartItemId(@Param("cartItemId") int cartItemId);



    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.ID = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") int orderId);

  @Query("SELECT oi FROM OrderItem oi WHERE oi.ord_item_status = :status")
  List<OrderItem> findByStatus(@Param("status") OrderItem.Status status);

}
