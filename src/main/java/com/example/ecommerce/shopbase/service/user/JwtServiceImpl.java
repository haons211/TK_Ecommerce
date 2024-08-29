package com.example.ecommerce.shopbase.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtServiceImpl implements JwtService {

    @NonFinal
    @Value("${jwt.token.time.expiration}")
    long JWT_TOKEN_TIME_EXPIRATION;

    @NonFinal
    @Value("${jwt.token.secret}")
    String JWT_SECRET;

    @NonFinal
    @Value("${jwt.token.issuer}")
    String JWT_ISSUER;

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuer(JWT_ISSUER)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_TIME_EXPIRATION))
                .signWith(getSigningKey(JWT_SECRET))
                .compact();
    }

    public SecretKey getSigningKey(String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long getExpiration(String token) {
        Date expiredDate = extractExpiredDate(token);
        return expiredDate.getTime() - new Date().getTime();
    }

    @Override
    public Boolean isJwtTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiredTime = extractExpiredDate(token);
        return expiredTime.before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiredDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(JWT_SECRET))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
