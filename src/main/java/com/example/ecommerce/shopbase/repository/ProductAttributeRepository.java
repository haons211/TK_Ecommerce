package com.example.ecommerce.shopbase.repository;


import com.example.ecommerce.shopbase.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Integer> {

    List<ProductAttribute> findAllByProductId(int id);

    void deleteAllByProductId(int id);

}
