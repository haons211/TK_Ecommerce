package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.dto.request.CreateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserRequest;
import com.example.ecommerce.shopbase.dto.response.UserAddressResponse;
import com.example.ecommerce.shopbase.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getMyInfo();

    UserResponse updateUser(Integer userId, UpdateUserRequest request);

    void createUserAddress(CreateUserAddressRequest request);

    List<UserAddressResponse> getUserAddressList();

    void updateUserAddress(UpdateUserAddressRequest request);

    void deleteUserAddress(Integer addressId);

    void setAddressDefault(Integer addressId);
}
