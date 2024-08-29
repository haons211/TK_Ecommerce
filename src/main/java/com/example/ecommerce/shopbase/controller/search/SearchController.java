package com.example.ecommerce.shopbase.controller.search;

import com.example.ecommerce.shopbase.dto.product.ProductDTO;
import com.example.ecommerce.shopbase.dto.response.AddressResponse;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.SearchProductResponse;
import com.example.ecommerce.shopbase.entity.ProductES;
import com.example.ecommerce.shopbase.service.search.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {

    SearchService searchService;

    @GetMapping("/searches")
    public ApiResponse<Page<ProductES>> searchProductES(@RequestParam("q") String query,
                                                        @RequestParam("page") int pageNumber,
                                                        @RequestParam("limit") int pageSize){
        return ApiResponse.<Page<ProductES>>builder()
                .result(searchService.searchProducts(query,pageNumber,pageSize))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<SearchProductResponse>> searchProduct(@RequestParam(value = "q") String query,
                                                                  @RequestParam(value = "type",required = false)String type,
                                                                  @RequestParam(value = "order",required = false)String order,
                                                                  @RequestParam(value = "minPrice",required = false)Integer minPrice,
                                                                  @RequestParam(value = "maxPrice",required = false)Integer maxPrice,
                                                                  @RequestParam(value = "page",defaultValue = "0") int pageNumber,
                                                                  @RequestParam(value = "limit",defaultValue = "30") int pageSize){
        return ApiResponse.<Page<SearchProductResponse>>builder()
                .result(searchService.getSearchedProducts(query,pageNumber,pageSize,type,order,minPrice,maxPrice))
                .build();
    }

    @GetMapping("/search/home-page")
    public ApiResponse<Page<SearchProductResponse>> searchProduct(Pageable pageable){
        return ApiResponse.<Page<SearchProductResponse>>builder()
                .result(searchService.getHomePage(pageable))
                .build();
    }

}
