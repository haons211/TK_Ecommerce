package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.request.ProductRequest;
import com.example.ecommerce.shopbase.dto.response.ProductDetailDTO;
import com.example.ecommerce.shopbase.dto.response.ProductResponse;
import com.example.ecommerce.shopbase.dto.response.SearchProductResponse;
import com.example.ecommerce.shopbase.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

    Product toProduct(ProductRequest productResponse);

    @Mapping(source = "imageDefault", target = "imageUrl")
    SearchProductResponse toSearchProductResponse(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "categoryId", source = "category.ID")
    @Mapping(target = "imageUrl", source = "imageDefault")
    ProductDetailDTO toProductDetailDTO(Product product);

}
