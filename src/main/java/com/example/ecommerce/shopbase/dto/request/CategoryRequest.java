package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @NotBlank(message = "Name must be not  blank")
    private String name;

    private String description;

    @Min(value = 0, message = "ParentId  must be greater or equal to 0")
    private Integer parentID;
    private String image;

    @Min(value = 0, message = "userId  must be greater or equal to 0")
    private Integer created_by_user;

}
