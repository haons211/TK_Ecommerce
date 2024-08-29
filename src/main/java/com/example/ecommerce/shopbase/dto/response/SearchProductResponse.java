package com.example.ecommerce.shopbase.dto.response;

import com.example.ecommerce.shopbase.entity.Category;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductAsset;
import com.example.ecommerce.shopbase.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchProductResponse {
    private Integer id;

    private String name;

    private String imageUrl;

    private Integer price;

    private Integer sold = 0;

}
