package com.example.ecommerce.shopbase.service.product;


import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Category;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductAttribute;
import com.example.ecommerce.shopbase.dto.request.ProductAttributeRequest;
import com.example.ecommerce.shopbase.dto.response.ProductAttributeResponse;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductAttributeRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import com.example.ecommerce.shopbase.entity.ProductAttribute;
import com.example.ecommerce.shopbase.repository.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {
    @Autowired
    ProductAttributeRepository repo ;


    @Autowired
    ProductAttributeRepository productRepo ;
    @Autowired
     ProductAttributeRepository productAttributeRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<ProductAttribute> getProductAttributesByProductId(int productId) {
        return repo.findAllByProductId(productId);
    }


    @Override
    public List<ProductAttribute> getAllProductAttributes() {
        return productRepo.findAll();
    }

    @Override
    public ProductAttribute add(ProductAttributeRequest productAttributeRequest) {
        Product product = productRepository.findById(productAttributeRequest.getProductId()).get();
       ProductAttribute productAttribute1 = ProductAttribute.builder()
               .product(product)
               .name(productAttributeRequest.getName())
               .value(productAttributeRequest.getValue())
               .product(product)
                .build();

        return productAttributeRepository.save(productAttribute1);
    }

    @Override
    public void delete(int id) {
        ProductAttribute value = productRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));;
            productRepo.delete(value);

    }

    @Override
    public ProductAttribute update(int id, ProductAttributeRequest updatedProductAttribute) {
        ProductAttribute existingProductAttribute = productAttributeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));;

        existingProductAttribute.setName(updatedProductAttribute.getName());
        existingProductAttribute.setValue(updatedProductAttribute.getValue());

        Product product = productRepository.findById(updatedProductAttribute.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        existingProductAttribute.setProduct(product);

        return productAttributeRepository.save(existingProductAttribute);
    }



}
