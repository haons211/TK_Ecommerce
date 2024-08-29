package com.example.ecommerce.shopbase.dto.product;

import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductAsset;
import com.example.ecommerce.shopbase.entity.ProductAttribute;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    Product product;
    List<ProductAsset> productAssets;
    List<ProductAttribute> attributes;
    List<AssortmentValueListDTO> assortments;
    
}
