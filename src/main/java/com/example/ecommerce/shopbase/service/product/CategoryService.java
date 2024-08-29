package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.CategoryRequest;
import com.example.ecommerce.shopbase.dto.response.CategoryResponse;
import com.example.ecommerce.shopbase.entity.Category;
import com.example.ecommerce.shopbase.entity.Product;

import java.util.List;

public interface CategoryService {
     Category addCategory(CategoryRequest category);
     List<Category> getAllCategories();
     void delete(int id);
     Category updateCategory(int id,CategoryRequest category);
     List<Category> getAllCategory0();
     List<Category> getAllCategoryByID(int id);
}
