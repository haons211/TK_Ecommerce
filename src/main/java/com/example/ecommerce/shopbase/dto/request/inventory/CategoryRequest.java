package com.example.ecommerce.shopbase.dto.request.inventory;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message = "Category Name must be not blank")
    private String name;


    private String description;
    private Integer parentID;
    private String image;


}
