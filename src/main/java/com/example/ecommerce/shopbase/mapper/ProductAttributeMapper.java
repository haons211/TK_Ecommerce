package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.response.ProductAttributeResponse;
import com.example.ecommerce.shopbase.entity.ProductAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProductAttributeMapper {
    ProductAttributeResponse productAttributeToProductAttributeResponse(ProductAttribute productAttribute);
}
