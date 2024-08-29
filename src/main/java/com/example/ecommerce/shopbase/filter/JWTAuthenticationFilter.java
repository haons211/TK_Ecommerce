package com.example.ecommerce.shopbase.filter;

import com.example.ecommerce.shopbase.service.user.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Objects;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;
    UserDetailsService userDetailsService;
    HandlerExceptionResolver handlerExceptionResolver;
    RedisTemplate<Object, Object> redisTemplate;

    @NonFinal
    @Value("${jwt.token.header.authorization}")
    String JWT_TOKEN_HEADER_AUTHORIZATION;

    @NonFinal
    @Value("${jwt.token.prefix}")
    String JWT_TOKEN_PREFIX;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JWT_TOKEN_HEADER_AUTHORIZATION);
        String jwtToken;
        String username;

        if(header == null || !header.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtToken = header.substring(JWT_TOKEN_PREFIX.length() + 1);
            username = jwtService.extractUsername(jwtToken);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String isLogout = (String) redisTemplate.opsForValue().get(jwtToken);

                if(jwtService.isJwtTokenValid(jwtToken)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            jwtToken,
                            userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                filterChain.doFilter(request, response);
            }
        } catch (JwtException | RedisConnectionFailureException exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
