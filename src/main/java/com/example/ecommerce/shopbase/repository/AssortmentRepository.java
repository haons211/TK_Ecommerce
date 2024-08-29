package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssortmentRepository extends JpaRepository<Assortment, Integer> {
    List<Assortment> findAllByProductId(Integer productId);
    void deleteAllByProductId(Integer productId);

    List<Assortment> findAllByProduct(Product product);
}