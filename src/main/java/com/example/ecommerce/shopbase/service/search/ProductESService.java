package com.example.ecommerce.shopbase.service.search;

import com.example.ecommerce.shopbase.entity.ProductES;
import com.example.ecommerce.shopbase.repository.ProductESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
public class ProductESService {

    @Autowired
    ProductESRepository productESRepository;

    public ProductES save(ProductES productES){
        return productESRepository.save(productES);
    }

    public List<ProductES> saveAll(List<ProductES> productESs){
        return (List<ProductES>) productESRepository.saveAll(productESs);
    }

    public void delete(ProductES productES){
        productESRepository.delete(productES);
    }

    public void deletes(List<ProductES> productESs){
        productESRepository.deleteAll(productESs);
    }

    public List<ProductES> getAllByProductId(List<String> productIds){
        return productESRepository.findAllByProductIds(productIds);
    }

    public ProductES getByProductId(String productId){
        return productESRepository.findByProductId(productId);
    }


}
