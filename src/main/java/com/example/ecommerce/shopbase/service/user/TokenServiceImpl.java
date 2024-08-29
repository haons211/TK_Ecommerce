package com.example.ecommerce.shopbase.service.user;


import com.example.ecommerce.shopbase.entity.Token;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.repository.TokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenServiceImpl implements TokenService {

    TokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.refreshtoken.time.expiration}")
    Long JWT_REFRESH_TOKEN_TIME_EXPIRATION;

    @NonFinal
    @Value("${auth.password.forgot.token.time.expiration}")
    Long AUTH_PASSWORD_FORGOT_TOKEN_TIME_EXPIRATION;

    @Override
    public Token generateRefreshToken(User user) {
        Token token = generateToken(user, Token.Type.REFRESH);
        token.setExpiredAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_TIME_EXPIRATION));
        return tokenRepository.save(token);
    }

    @Override
    public Token generateForgotPasswordToken(User user) {
        Token token = generateToken(user, Token.Type.FORGOT_PASSWORD);
        token.setExpiredAt(new Date(System.currentTimeMillis() + AUTH_PASSWORD_FORGOT_TOKEN_TIME_EXPIRATION));
        return tokenRepository.save(token);
    }

    private Token generateToken(User user, Token.Type type) {
        return Token.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .type(type)
                .build();
    }
}
