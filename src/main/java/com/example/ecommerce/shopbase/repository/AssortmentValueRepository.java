package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssortmentValueRepository extends JpaRepository<Value,Long> {
    List<Value> findAllByAssortmentID(Integer id);
}
