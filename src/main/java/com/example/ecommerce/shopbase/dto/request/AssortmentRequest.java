package com.example.ecommerce.shopbase.dto.request;

import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Value;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssortmentRequest {
    @NotBlank(message = "Name must be not blank")
    String name;

    @NotNull(message = "ProductId must not be null")
    @Min(value = 1, message = "ProductId must be greater than 0")
    Integer productId;

    @NotNull
    List<ValueRequest> valueRequestList;
}