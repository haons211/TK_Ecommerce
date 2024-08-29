package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.request.UpdateUserRequest;
import com.example.ecommerce.shopbase.dto.request.UserCreationRequest;
import com.example.ecommerce.shopbase.dto.response.UserResponse;
import com.example.ecommerce.shopbase.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    void updateUser(@MappingTarget User user, UpdateUserRequest request);

    UserResponse toUserResponse(User user);
}
