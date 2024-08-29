package com.example.ecommerce.shopbase.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "product")
public class ProductES {

    @Id
    String id;

    int product_id;

    String name;

    String nameWithoutAccent;

    String description;

}
