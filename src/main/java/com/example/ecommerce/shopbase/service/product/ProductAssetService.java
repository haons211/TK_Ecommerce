package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.entity.ProductAsset;

import java.util.List;

public interface ProductAssetService {
    ProductAsset saveImg (Integer productId, String filename);

    void   deleteImage(String filename);
    List<String> getImg(Integer productId);
    List<ProductAsset> getProductAssetList(Integer productId);
}
