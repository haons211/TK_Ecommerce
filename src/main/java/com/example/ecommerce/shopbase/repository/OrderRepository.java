package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE o.order_code = :orderCode")
    Order findByOrderCode(@Param("orderCode") String orderCode);
}
