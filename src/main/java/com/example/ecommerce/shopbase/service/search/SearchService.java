package com.example.ecommerce.shopbase.service.search;

import com.example.ecommerce.shopbase.dto.product.ProductDTO;
import com.example.ecommerce.shopbase.dto.response.SearchProductResponse;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SearchService {
    Page<ProductES> searchProducts(String queryString,int pageNumber,int pageSize);
    Page<ProductES> searchProducts(String queryString, String fuzzyField,int pageNumber,int pageSize);
    Page<SearchProductResponse> getSearchedProducts(String queryString, int pageNumber, int pageSize, String type, String order, Integer minPrice, Integer maxPrice);
    Page<SearchProductResponse> getHomePage(Pageable pageable);
    List<SearchProductResponse> getProductDetailResponse(List<Integer> listId);
}
