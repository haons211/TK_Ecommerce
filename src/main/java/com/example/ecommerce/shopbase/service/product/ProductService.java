package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.product.ProductDTO;
import com.example.ecommerce.shopbase.dto.product.ProductValueCombination;
import com.example.ecommerce.shopbase.dto.request.ProductRequest;
import com.example.ecommerce.shopbase.dto.response.ProductDetailDTO;
import com.example.ecommerce.shopbase.dto.response.ProductFullResponse;
import com.example.ecommerce.shopbase.dto.response.ProductResponse;
import com.example.ecommerce.shopbase.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getallProduct();

    ProductDTO getProductAndAttributeAssortment(int id);

    ProductDTO getProductbyId(int id);

    Page<ProductFullResponse> getProductbySeller(int seller_id, Pageable pageable);

    Page<ProductDetailDTO> getProductbySellerForSeller(Pageable pageable);

    ProductResponse addProduct(ProductRequest productRequestDTO);

    ProductResponse updateProduct(int id, ProductRequest productRequestDTO);

    void deleteProduct(int id);

    Integer getSellerId(int id);

    ProductDTO getFullInfoProduct(int id);

    ProductValueCombination getProductValueCombination(Product product);

    Page<Product> getProductByCategory(int categoryId, Pageable pageable);

    List<List<String>> getFileByProductId(int productId);

    List<List<String>> getFilePathsByProductId(int productId);

    Integer getSoldbySeller();

    Integer getQuantitybySeller();

    List<ProductFullResponse> getFullProductDetails(Integer productId);

    ProductDetailDTO getProductDetail(Integer productId);

    Page<Product> getSoldOutProductBySeller(Pageable pageable);

    Map<String, Object> getRevenueBySeller();

    Double getSumRevenueBySeller();

}


