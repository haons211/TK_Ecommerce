package com.example.ecommerce.shopbase.service.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

public interface JwtService {
    String generateToken(String username);

    Key getSigningKey(String jwtSecret);

    String extractUsername(String token);

    Long getExpiration(String token);

    Boolean isJwtTokenValid(String token);
}
