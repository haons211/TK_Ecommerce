package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.ProductAttributeRequest;
import com.example.ecommerce.shopbase.dto.response.ProductAttributeResponse;
import com.example.ecommerce.shopbase.entity.ProductAttribute;

import java.util.List;

public interface ProductAttributeService {
    List<ProductAttribute> getProductAttributesByProductId(int productId);
    List<ProductAttribute> getAllProductAttributes();
    ProductAttribute add(ProductAttributeRequest productAttribute);
    void delete(int id);
    ProductAttribute update(int id, ProductAttributeRequest productAttributeRequest);



}
