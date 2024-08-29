package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.response.ValueCombinationResponse;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ValueCombinationMapper {
    @Mapping(target = "value1", expression = "java(valueCombination.getValue1() != null ? valueCombination.getValue1().getValue() : null)")
    @Mapping(target = "value2", expression = "java(valueCombination.getValue2() != null ? valueCombination.getValue2().getValue() : null)")
    ValueCombinationResponse toValueCombinationResponse(ValueCombination valueCombination);
}