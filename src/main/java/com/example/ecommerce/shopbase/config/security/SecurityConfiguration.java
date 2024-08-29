package com.example.ecommerce.shopbase.config.security;

import com.example.ecommerce.shopbase.filter.JWTAuthenticationFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement(order = 0)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfiguration {

    JWTAuthenticationFilter jwtAuthenticationFilter;

    AuthenticationManager authenticationManager;

  @Bean
  public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/api/v1/test").permitAll()
            .requestMatchers("/api/v1/address/**").permitAll()
            .requestMatchers("/api/v1/search/**").permitAll()
            .requestMatchers("/api/v1/category/**").permitAll()
            .requestMatchers("/api/v1/category/**").permitAll()
            .requestMatchers("/api/v1/product/**").permitAll()
            .requestMatchers("/api/v1/seller/**").permitAll()
            .requestMatchers("/api/v1/order-item/**").permitAll()
            .requestMatchers("/api/v1/payments/vnpay_ipn").permitAll()
                .requestMatchers("/api/v1/payments/vnpay-payment-return").permitAll()
            .requestMatchers("/api/v1/auth/password/change-password").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/v1/user/{userId}").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/v1/seller/{sellerId}").authenticated()
            .requestMatchers(HttpMethod.PUT, "/test-inventory").permitAll()
            .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .authenticationManager(authenticationManager)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
            .accessDeniedHandler(new JwtAuthenticationEntryPoint()));
    return http.build();
  }

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}