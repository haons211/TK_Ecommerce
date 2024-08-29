package com.example.ecommerce.shopbase.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValueCombinationRequest {
    private List<@NotNull @Min(value = 1, message = "ValueId must be greater than or equal to 1") Integer> valueIds;
}
