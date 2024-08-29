package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.ProductRequest;
import com.example.ecommerce.shopbase.dto.response.*;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.service.product.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productServiceImpl;
    ProductAttributeService productAttributeServiceImpl;
    ProductAssetServiceImpl productAssetServiceImpl;
    ValueCombinationServiceImpl valueCombinationServiceImpl;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProduct() {
        List<Product> products = productServiceImpl.getallProduct();
        List<ProductResponse> responseList = new ArrayList<>();
        for (Product product : products) {
            List<String> filepaths = productAssetServiceImpl.getImg(product.getId());
            ProductResponse response = ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getID())
                .imageUrl(filepaths.isEmpty() ? "" : filepaths.getFirst())
                .sold(product.getSold())
                .build();
            responseList.add(response);
        }
        return ApiResponse.<List<ProductResponse>>builder()
            .result(responseList)
            .build();
    }


    @GetMapping("/get-product-by-seller")
    public ApiResponse<Page<ProductFullResponse>> getProductBySeller(@RequestParam("sellerId") int sellerId, Pageable pageable) {
        Page<ProductFullResponse> products = productServiceImpl.getProductbySeller(sellerId, pageable);
        return ApiResponse.<Page<ProductFullResponse>>builder()
            .result(products)
            .build();

    }

    @GetMapping("/get-product-by-seller-seller")
    public ApiResponse<Page<ProductDetailDTO>> getProductBySellerForSeller(Pageable pageable) {
        Page<ProductDetailDTO> products = productServiceImpl.getProductbySellerForSeller(pageable);
        return ApiResponse.<Page<ProductDetailDTO>>builder()
            .result(products)
            .build();

    }


    @GetMapping("/get-attribute-by-product")
    public ApiResponse<List<ProductAttribute>> getAttributeByProduct(@RequestParam("productId") int productId) {
        List<ProductAttribute> productAttributes = productAttributeServiceImpl.getProductAttributesByProductId(productId);
        return ApiResponse.<List<ProductAttribute>>builder()
            .result(productAttributes)
            .build();

    }



    @PostMapping
    public ApiResponse<ProductResponse> addProduct(@RequestBody @Valid ProductRequest productRequestDTO) {
        ProductResponse productResponse = productServiceImpl.addProduct(productRequestDTO);
        return ApiResponse.<ProductResponse>builder()
            .result(productResponse)
            .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable("id") int id, @RequestBody @Valid ProductRequest productRequestDTO) {
        ProductResponse productResponse = productServiceImpl.updateProduct(id, productRequestDTO);
        return ApiResponse.<ProductResponse>builder()
            .result(productResponse)
            .build();

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable("id") int id) {
        productServiceImpl.deleteProduct(id);
        return ApiResponse.<Void>builder()
            .message("Successfully deleted product")
            .build();


    }

    @GetMapping("/get-product-by-category/{categoryId}")
    public ApiResponse<Page<ProductResponse>> getAllProductByCategory(@PathVariable("categoryId") int categoryId, Pageable pageable) {
        Page<Product> products = productServiceImpl.getProductByCategory(categoryId, pageable);

        Page<ProductResponse> responseListPage = products.map(product -> {
            List<String> filepaths = productAssetServiceImpl.getImg(product.getId());
            return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .categoryId(categoryId)
                .imageUrl(filepaths.isEmpty() ? "" : filepaths.getFirst())
                .sold(product.getSold())
                .build();
        });


        return ApiResponse.<Page<ProductResponse>>builder()
            .result(responseListPage)
            .build();
    }

    @PostMapping("/save-img/")
    public ApiResponse<ProductAssetResponse> saveImg(@RequestParam Integer productId, @RequestParam String filename) {
        ProductAsset productAsset = productAssetServiceImpl.saveImg(productId, filename);

        ProductAssetResponse productAssetResponse = ProductAssetResponse.builder()
                .productId(productId)
                .name(productAsset.getName())
                .build();

        return ApiResponse.<ProductAssetResponse>builder()
                .result(productAssetResponse)
                .build();
    }

    @GetMapping("/get-img-url/{productId}")
    public ApiResponse<List<String>> getImg(@PathVariable("productId") int productId) {
        List<String> filepaths = productAssetServiceImpl.getImg(productId);
        return ApiResponse.<List<String>>builder()
            .result(filepaths)
            .build();
    }


    @DeleteMapping("/delete-image/{fileName}")
    public ApiResponse<Void> deleteImg(@PathVariable("fileName") String fileName) {
        productAssetServiceImpl.deleteImage(fileName);
        return ApiResponse.<Void>builder()
            .message("Successfully deleted product")
            .build();
    }

    @GetMapping("/getSumSold")
    public ApiResponse<Integer> getSold() {
        return ApiResponse.<Integer>builder()
            .result(productServiceImpl.getSoldbySeller())
            .build();
    }

    @GetMapping("/getSumQuantity")
    public ApiResponse<Integer> getQuantity() {
        return ApiResponse.<Integer>builder()
            .result(productServiceImpl.getQuantitybySeller())
            .build();
    }


    @GetMapping("/get-value-combination/{productId}")
    public ApiResponse<List<ValueCombinationResponse>> getValueCombination(@PathVariable("productId") int productId) {
        List<ValueCombination> valueCombinations = valueCombinationServiceImpl.getAllValueCombinationsbyProductId(productId);

        List<ValueCombinationResponse> responseList = new ArrayList<>();
        for (ValueCombination valueCombination : valueCombinations) {
            ValueCombinationResponse response = ValueCombinationResponse.builder()
                    .id(valueCombination.getId())
                    .productId(productId)
                    .value1(valueCombination.getValue1().getValue())
                    .value2(valueCombination.getValue2() != null ? valueCombination.getValue2().getValue() : null)
                    .price(valueCombination.getPrice())
                    .quantity(valueCombination.getQuantity())
                    .build();
            responseList.add(response);
        }

        return ApiResponse.<List<ValueCombinationResponse>>builder()
                .result(responseList)
                .build();
    }


    @GetMapping("/get-full-in4-product/{productId}")
    public ApiResponse<List<ProductFullResponse>> getFullIn4Product(@PathVariable("productId") int productId) {
        return ApiResponse.<List<ProductFullResponse>>builder()
                .result(productServiceImpl.getFullProductDetails(productId))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailDTO> getProductDetail(@PathVariable Integer productId) {
        return ApiResponse.<ProductDetailDTO>builder()
                .result(productServiceImpl.getProductDetail(productId))
                .build();
    }


    @GetMapping("/get-product-sold-out")
    public ApiResponse<Page<ProductResponse>> getProductSoldOut(Pageable pageable) {
        Page<Product> products = productServiceImpl.getSoldOutProductBySeller(pageable);
        Page<ProductResponse> responseListPage = products.map(product -> {
            List<String> filepaths = productAssetServiceImpl.getImg(product.getId());
            return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getID())
                .imageUrl(filepaths.isEmpty() ? "" : filepaths.getFirst())
                .sold(product.getSold())
                .build();
        });


        return ApiResponse.<Page<ProductResponse>>builder()
            .result(responseListPage)
            .build();
    }


    @GetMapping("/get-revenue-by-month-year")
    public ApiResponse<Map<String, Object>> getRevenue() {
        return ApiResponse.<Map<String, Object>>builder()
            .result(productServiceImpl.getRevenueBySeller())
            .build();
    }

    @GetMapping("/get-sum-revenue")
    public ApiResponse<Double> getSumRevenue() {
        return ApiResponse.<Double>builder()
            .result(productServiceImpl.getSumRevenueBySeller())
            .build();
    }

}
