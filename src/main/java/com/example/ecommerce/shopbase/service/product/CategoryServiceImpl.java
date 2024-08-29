package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.CategoryRequest;
import com.example.ecommerce.shopbase.dto.response.CategoryResponse;
import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Category;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.CategoryRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductServiceImpl productServiceImpl;


    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        Category category1 = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .created_by_user(categoryRequest.getCreated_by_user())
                .parentID(categoryRequest.getParentID())
                .image(categoryRequest.getImage())
                .build();
        return categoryRepository.save(category1);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void delete(int id){
        Category category=categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        List<Product> products=productRepository.findProductByCategoryID(id);
        for (Product product : products) {
            productServiceImpl.deleteProduct(product.getId());
        }

        categoryRepository.deleteById(id);
    }


    @Override
    public Category updateCategory(int id, CategoryRequest updatedCategoryRequest) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        existingCategory.setName(updatedCategoryRequest.getName());
        existingCategory.setDescription(updatedCategoryRequest.getDescription());
        existingCategory.setCreated_by_user(updatedCategoryRequest.getCreated_by_user());

        if (updatedCategoryRequest.getParentID() != null) {
            Category parent = categoryRepository.findById(updatedCategoryRequest.getParentID())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            existingCategory.setParentID(parent.getParentID());
        } else {
            existingCategory.setParentID(null);
        }

        existingCategory.setImage(updatedCategoryRequest.getImage());

        return categoryRepository.save(existingCategory);
    }





    @Override
    public List<Category> getAllCategory0() {
        List<Category> categories = categoryRepository.findCategoriesByParentID(0);
      return categories;
    }

    @Override
    public List<Category> getAllCategoryByID(int id) {
        Category category=categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        List<Category> categoryList=categoryRepository.findCategoriesByParentID(id);
        return categoryList;
    }
}
