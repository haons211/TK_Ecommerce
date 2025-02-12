package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
        List<Category> findCategoriesByParentID(int parentID);
}
