package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.request.*;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.LoginResponse;
import com.example.ecommerce.shopbase.dto.response.RefreshTokenResponse;
import com.example.ecommerce.shopbase.service.user.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AuthController {

    AuthService authService;

    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody UserCreationRequest request) {
        authService.register(request);
        return ApiResponse.<String>builder()
                .message("We have sent a email. Please check email to active account!")
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse result = authService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/refreshToken")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestParam String refreshToken) {
        RefreshTokenResponse result = authService.refreshToken(refreshToken);

        return ApiResponse.<RefreshTokenResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/registration/active")
    public ApiResponse<String> activeAccount(
            @RequestBody @Valid ActiveAccountRequest activeAccountRequest
    ) {
        authService.activeAccount(activeAccountRequest);

        return ApiResponse.<String>builder()
                .message("Active User Successfully")
                .build();
    }

    @GetMapping("/registration/active-mail")
    public ApiResponse<String> resendEmailActive(@RequestParam String email) {
        authService.sendActiveEmail(email);

        return ApiResponse.<String>builder()
                .message("We have sent a email. Please check email to active account!")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authService.logout(request);

        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @PostMapping("/password/forgot-mail")
    public ApiResponse<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);

        return ApiResponse.<Void>builder()
                .message("We have sent a email. Please check email!")
                .build();
    }

    @PostMapping("/password/reset")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @PutMapping("/password/change-password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);

        return ApiResponse.<Void>builder()
                .message("Change password success")
                .build();
    }
}
