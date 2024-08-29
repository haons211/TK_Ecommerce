package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.response.ProductAssetResponse;
import com.example.ecommerce.shopbase.entity.ProductAsset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProductAssetMapper {
    ProductAssetResponse productAssetToProductAssetResponse(ProductAsset productAsset);
}

