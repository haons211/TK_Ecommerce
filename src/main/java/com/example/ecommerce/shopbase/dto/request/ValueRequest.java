package com.example.ecommerce.shopbase.dto.request;

import com.example.ecommerce.shopbase.entity.Assortment;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValueRequest {
    @NotBlank(message = "Value must be not blank")
    private String value;

    @Min(value = 0, message = "AssortmentId must be greater or equal to 0")
    private Integer assortmentId;

}
