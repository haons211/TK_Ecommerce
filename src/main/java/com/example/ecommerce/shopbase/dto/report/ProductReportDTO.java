package com.example.ecommerce.shopbase.dto.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReportDTO {
    private Integer id;
    private String name;
    private Integer quantity;

}
