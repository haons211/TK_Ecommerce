package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductAsset;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductAssetRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.service.storage.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAssetServiceImpl implements ProductAssetService {
        @Autowired
        private ProductRepository productRepository;
        @Autowired
        private ProductAssetRepository productAssetRepository;
    @Autowired
    private StorageServiceImpl storageServiceImpl;


    @Override
        public ProductAsset saveImg(Integer productId, String filename) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            if(product.getImageDefault() == null) {
                product.setImageDefault(filename);
                productRepository.save(product);
            }
            ProductAsset productAssetResponse = ProductAsset.builder()
                    .product(product)
                    .name(filename)
                    .build();

            return productAssetRepository.save(productAssetResponse);
        }
    @Override
    public void  deleteImage(String filename) {
        ProductAsset productAsset=productAssetRepository.findFirstByName(filename).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productAssetRepository.delete(productAsset);
        }


    @Override
    public List<String> getImg(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ProductAsset> productAssets = productAssetRepository.findAllByProductId(productId);

       List<String> filePaths = new ArrayList<>();
       for (ProductAsset productAsset : productAssets) {
           filePaths.add(productAsset.getName());

       }
       Collections.sort(filePaths);

        List<String> imageUrls = new ArrayList<>();
        for (String filePath : filePaths) {
            imageUrls.add(storageServiceImpl.getObjectURL(filePath));
        }

        return imageUrls;
    }



    @Override
    public List<ProductAsset> getProductAssetList(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ProductAsset> productAssets = productAssetRepository.findAllByProductId(productId);

        return productAssets;
    }


}


