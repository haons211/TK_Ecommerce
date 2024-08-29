package com.example.ecommerce.shopbase.dto.product;


import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Value;

import java.io.Serializable;
import java.util.List;

public record AssortmentValueListDTO(Assortment assortment, List<Value> valueList) {
}
