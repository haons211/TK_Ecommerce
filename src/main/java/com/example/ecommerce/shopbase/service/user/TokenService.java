package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.entity.Token;
import com.example.ecommerce.shopbase.entity.User;

public interface TokenService {

    Token generateRefreshToken(User user);
    Token generateForgotPasswordToken(User user);
}
