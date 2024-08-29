package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.CategoryRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.CategoryResponse;
import com.example.ecommerce.shopbase.entity.Category;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.CategoryRepository;
import com.example.ecommerce.shopbase.service.product.CategoryService;
import com.example.ecommerce.shopbase.service.product.ProductAttributeService;
import com.example.ecommerce.shopbase.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    CategoryService categoryServiceImpl;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ApiResponse<Category> add(@Valid @RequestBody CategoryRequest category) {
        Category savedCategory = categoryServiceImpl.addCategory(category);
        return ApiResponse.<Category>builder()
                .result(savedCategory)
                .build();
    }
    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
            List<Category> categories = categoryServiceImpl.getAllCategories();
            return ApiResponse.<List<Category>>builder()
                    .result(categories)
                    .build();
        }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable("id") int id) {
        categoryServiceImpl.delete(id);
        return ApiResponse.<Void>builder()
                .message("Category deleted")
                .build();

    }

    @GetMapping("/list-Category0")
    public ApiResponse<List<CategoryResponse>> listCategory0() {
        List<Category> categories = categoryServiceImpl.getAllCategory0();
        List<CategoryResponse> responseList = new ArrayList<>();
        for (Category category : categories) {
            responseList.add(CategoryResponse.builder()
                            .name(category.getName())
                            .id(category.getID())
                    .build());
        }
        return ApiResponse.<List<CategoryResponse>>builder()
                        .result(responseList)
                        .build();
    }



    @PutMapping("/{id}")
    public ApiResponse<Category> updateCategory(@PathVariable("id") int id, @RequestBody CategoryRequest updatedCategory) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        Category updatedEntity = categoryServiceImpl.updateCategory(id, updatedCategory);
            return ApiResponse.<Category>builder()
                    .result(updatedEntity)
                    .build();
    }

    @GetMapping("/get-category-by-parentId/{id}")
    public ApiResponse<List<CategoryResponse>> getCategoryByParentId(@PathVariable("id") int id) {
        List<Category> categories = categoryServiceImpl.getAllCategoryByID(id);
        List<CategoryResponse> responseList = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse response = CategoryResponse.builder()
                    .name(category.getName())
                    .id(category.getID())
                    .build();
            responseList.add(response);
        }
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(responseList)
                .build();
    }
}

