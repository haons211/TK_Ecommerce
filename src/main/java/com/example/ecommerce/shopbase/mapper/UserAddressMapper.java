package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.request.CreateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.response.UserAddressResponse;
import com.example.ecommerce.shopbase.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddress toUserAddress(CreateUserAddressRequest request);

    UserAddressResponse toUserAddressResponse(UserAddress userAddress);

    void updateUserAddress(@MappingTarget UserAddress address, UpdateUserAddressRequest request);

}
