package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Seller;
import com.example.ecommerce.shopbase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

    Boolean existsByUserOrName(User user, String name);

    Optional<Seller> findByUser(User user);

    Seller findSellersByUserId(Integer userId);

//    @Query(
//        "SELECT " +
//            "   MONTH(o.completed_at) AS month, " +
//            "   YEAR(o.completed_at) AS year, " +
//            "   SUM(vc.price * ci.quantity) AS totalRevenue " +
//            "FROM Order o " +
//            "JOIN o.order_item oi " +
//            "JOIN oi.cartItem ci " +
//            "JOIN ci.cartItemAttributes cia " +
//            "JOIN cia.valueCombination vc " +
//            "JOIN vc.product p " +
//            "WHERE p.seller.id = :sellerId " +
//            "AND o.status = 'succeeded' " +
//            "GROUP BY MONTH(o.completed_at), YEAR(o.completed_at) " +
//            "ORDER BY year, month"
//    )
//    List<Map<String, Object>> getTotalRevenueByMonthForSeller(@Param("sellerId") Integer sellerId);
}