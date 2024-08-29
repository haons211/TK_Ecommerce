package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.dto.request.*;
import com.example.ecommerce.shopbase.dto.response.LoginResponse;
import com.example.ecommerce.shopbase.dto.response.RefreshTokenResponse;

public interface AuthService {
    void register(UserCreationRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);

    void activeAccount(ActiveAccountRequest request);

    void sendActiveEmail(String email);

    void logout(LogoutRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(ChangePasswordRequest request);
}
