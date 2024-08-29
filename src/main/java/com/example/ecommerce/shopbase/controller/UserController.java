package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.request.CreateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.SetDefaultAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.UserAddressResponse;
import com.example.ecommerce.shopbase.dto.response.UserResponse;
import com.example.ecommerce.shopbase.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class UserController {

    UserService userService;

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Integer userId,
            @RequestBody UpdateUserRequest request) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @PostMapping("/address/create_user_address")
    public ApiResponse<String> createUserAddress(@RequestBody @Valid CreateUserAddressRequest request) {
        userService.createUserAddress(request);
        return ApiResponse.<String>builder()
                .message("Success")
                .build();
    }

    @GetMapping("/address/get_user_address_list")
    public ApiResponse<List<UserAddressResponse>> getUserAddressList() {
        return ApiResponse.<List<UserAddressResponse>>builder()
                .result(userService.getUserAddressList())
                .build();
    }

    @PutMapping("/address/update_user_address")
    public ApiResponse<Void> updateUserAddress(@RequestBody @Valid UpdateUserAddressRequest request) {
        userService.updateUserAddress(request);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @PostMapping("/address/set_default_address")
    public ApiResponse<Void> setDefaultAddress(@RequestBody @Valid SetDefaultAddressRequest request) {
        userService.setAddressDefault(request.getAddressId());
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @DeleteMapping("/address/{addressId}")
    public ApiResponse<Void> deleteUserAddress(@PathVariable Integer addressId) {
        userService.deleteUserAddress(addressId);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }
}
