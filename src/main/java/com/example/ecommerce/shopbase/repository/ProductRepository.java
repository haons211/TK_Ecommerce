package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.dto.report.ProductReportDTO;
import com.example.ecommerce.shopbase.dto.response.ProductDetailResponse;
import com.example.ecommerce.shopbase.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

import java.util.List;
import java.util.Map;


import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findProductByCategoryID(int categoryID, Pageable pageable);

    List<Product> findProductByCategoryID(int categoryID);

    Page<Product> findAllBySellerId(int sellerId, Pageable pageable);


    @Query(value = "SELECT p.* FROM product p WHERE p.ID IN :list order BY price DESC",nativeQuery = true)
    List<Product> orderByPriceDesc(List<Integer> list);

    @Query(value = "SELECT p.* FROM product p WHERE p.ID IN :list order BY price ASC",nativeQuery = true)
    List<Product> orderByPriceAsc(List<Integer> list);

    @Query(value = "SELECT p.* FROM product p WHERE p.ID IN :list order BY sold DESC",nativeQuery = true)
    List<Product> orderBySoldDesc(List<Integer> list);

    @Query("SELECT s.id FROM Seller s WHERE s.user = :userID")
    Integer getIdByUserID(int userID);

    Page<Product> findAllByOrderBySold(Pageable pageable);

    @Query("SELECT SUM(p.sold) FROM Product p WHERE p.seller.id = :sellerId")
    Integer getTotalSoldBySellerId(Integer sellerId);
    @Query("SELECT SUM(p.quantity) FROM Product p WHERE p.seller.id = :sellerId")
    Integer getTotalQuantityBySellerId(Integer sellerId);

    @Query(
        value =
            "SELECT " +
                "   DATE_FORMAT(o.completed_at, '%m') AS month, " +
                "   DATE_FORMAT(o.completed_at, '%Y') AS year, " +
                "   SUM(vc.price * ci.quantity) AS total_revenue " +
                "FROM `order` o " +
                "JOIN `order_item` oi ON o.id = oi.order_id " +
                "JOIN `cart_item_attribute` cia ON oi.cart_item_id = cia.cart_item_id " +
                "JOIN `value_combination` vc ON cia.value_combination_id = vc.id " +
                "JOIN `cart_item` ci ON oi.cart_item_id = ci.id " +
                "JOIN `product` p ON vc.product_id = p.id " +
                "WHERE o.status = 'successed' " +
                "AND p.seller_id = :sellerId " +
                "GROUP BY DATE_FORMAT(o.completed_at, '%m'), DATE_FORMAT(o.completed_at, '%Y') " +
                "ORDER BY DATE_FORMAT(o.completed_at, '%Y'), DATE_FORMAT(o.completed_at, '%m')",
        nativeQuery = true
    )
    List<Map<String, Object>> getTotalRevenueByMonthForSeller(@Param("sellerId") Integer sellerId);


    @Query(value = "SELECT p.id, p.name, SUM(t.quantity) as quantity\n" +
            "FROM product p \n" +
            "INNER JOIN (\n" +
            "    SELECT ci.product_id, ci.quantity\n" +
            "    FROM `order` o \n" +
            "    INNER JOIN order_item oi ON o.ID = oi.order_id \n" +
            "    INNER JOIN cart_item ci ON ci.ID = oi.cart_item_id \n" +
            "    WHERE o.completed_at BETWEEN :startDate AND :endDate\n" +
            ") t ON p.ID = t.product_id\n" +
            "WHERE p.seller_id = :sellerId\n" +
            "GROUP BY p.id, p.name\n" +
            "order BY quantity desc\n" +
            "LIMIT 100",nativeQuery = true)
    List<Object> getProductReport(@Param("sellerId") int sellerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM product WHERE status = :status", nativeQuery = true)
    List<Product> findProductsByStatus(@Param("status") String status);
    }

