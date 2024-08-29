package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.response.AddressResponse;
import com.example.ecommerce.shopbase.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "type", target = "type", ignore = true)
    AddressResponse toAddressResponse(Address address);
}
