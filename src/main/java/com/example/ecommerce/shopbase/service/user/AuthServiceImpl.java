package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.constant.PredefinedRole;
import com.example.ecommerce.event.NotificationEvent;
import com.example.ecommerce.shopbase.dto.request.*;
import com.example.ecommerce.shopbase.dto.response.LoginResponse;
import com.example.ecommerce.shopbase.dto.response.RefreshTokenResponse;
import com.example.ecommerce.shopbase.entity.Role;
import com.example.ecommerce.shopbase.entity.Token;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.RoleRepository;
import com.example.ecommerce.shopbase.repository.TokenRepository;
import com.example.ecommerce.shopbase.repository.UserRepository;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    @NonFinal
    @Value("${client.host}")
    String CLIENT_HOSTNAME;

    @NonFinal
    @Value("${client.protocol}")
    String CLIENT_PROTOCOL;

    @NonFinal
    @Value("${client.port}")
    Integer CLIENT_PORT;

    UserRepository userRepository;

    RoleRepository roleRepository;

    TokenRepository tokenRepository;

    OtpService otpService;

    JwtService jwtService;

    TokenService tokenService;

    SecurityUtils securityUtils;

    PasswordEncoder passwordEncoder;

    RedisTemplate<Object, Object> redisTemplate;

    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public void register(UserCreationRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXITED);

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE)
                .ifPresent(roles::add);

        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .lastChangePasswordDateTime(new Date(System.currentTimeMillis()))
                .roles(roles)
                .build();

        String otp = otpService.getOTP(request.getEmail());

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("[GHTK-ECOM] Active Account")
                .body("This organization has sent you an otp. This otp expires in 2 minutes. " + otp)
                .build();

        kafkaTemplate.send("notification-delivery", event);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername();

        User user = userRepository.findByUsernameOrEmail(username, username);

        if(user.getStatus().equals(User.Status.BLOCKED))
            throw new AppException(ErrorCode.USER_BLOCKED);

        tokenRepository.deleteByTypeAndUser(Token.Type.REFRESH, user);

        String jwtToken = jwtService.generateToken(username);
        Token refreshToken = tokenService.generateRefreshToken(user);

        return LoginResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(String refreshToken) {

        Token oldToken = tokenRepository.findByTypeAndToken(Token.Type.REFRESH, refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_EXISTED));

        tokenRepository.deleteByTypeAndUser(Token.Type.REFRESH, oldToken.getUser());

        Token newRefreshToken = tokenService.generateRefreshToken(oldToken.getUser());
        String newJwtToken = jwtService.generateToken(oldToken.getUser().getUsername());

        return RefreshTokenResponse.builder()
                .token(newJwtToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public void activeAccount(ActiveAccountRequest request) {
        String email = request.getEmail();
        String otpCode = request.getOtp();

        String hashOtp = (String) redisTemplate.opsForValue().get(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(!otpService.verifyOTP(otpCode, hashOtp))
            throw new AppException(ErrorCode.INVALID_OTP);

        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
        redisTemplate.opsForValue().getAndDelete(email);
    }

    @Override
    @Transactional
    public void sendActiveEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(user.getStatus() == User.Status.ACTIVE)
            throw new AppException(ErrorCode.USER_ACTIVATED);

        String otp = otpService.getOTP(email);

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(email)
                .subject("[GHTK-ECOM] Active Account")
                .body("This organization has sent you an otp. This otp expires in 2 minutes. " + otp)
                .build();

        kafkaTemplate.send("notification-delivery", event);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        String token = request.getToken();

        if(!jwtService.isJwtTokenValid(token))
            throw new AppException(ErrorCode.INVALID_TOKEN);

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsernameOrEmail(username, username);

        if(user == null)
            throw new AppException(ErrorCode.INVALID_TOKEN);

        tokenRepository.deleteByTypeAndUser(Token.Type.REFRESH, user);

        Long ttl = jwtService.getExpiration(token);

        redisTemplate.opsForValue()
                .set(token, "logout", ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        String usernameOrEmail = request.getUsernameOrEmail();

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if(user == null)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        tokenRepository.deleteByTypeAndUser(Token.Type.FORGOT_PASSWORD, user);
        Token tokenInfo = tokenService.generateForgotPasswordToken(user);

        String forgotPasswordURL = String.format("%s://%s:%d/auth/resetPassword/new-password?token=%s",
                CLIENT_PROTOCOL,
                CLIENT_HOSTNAME,
                CLIENT_PORT,
                tokenInfo.getToken());

        String subject = "[GHTK-ECOM] Reset Password";
        String content = "You have just sent a forgot password request\n"
                + "Click on the link below to get new password\n" + forgotPasswordURL;

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(user.getEmail())
                .subject(subject)
                .body(content)
                .build();

        kafkaTemplate.send("notification-delivery", event);

    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Token token = tokenRepository.findByTypeAndToken(Token.Type.FORGOT_PASSWORD, request.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_EXISTED));

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastChangePasswordDateTime(new Date(System.currentTimeMillis()));
        userRepository.save(user);

        // delete old forgot-password token
        tokenRepository.deleteByTypeAndUser(Token.Type.FORGOT_PASSWORD, user);

        // force user logout and login again
        tokenRepository.deleteByTypeAndUser(Token.Type.REFRESH, user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = securityUtils.getCurrentUserLogin();
        String jwt = securityUtils.getCurrentUserToken();

        boolean isValid = passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        );

        if(!isValid)
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastChangePasswordDateTime(new Date(System.currentTimeMillis()));
        userRepository.save(user);

        // force user logout and login again
        tokenRepository.deleteByTypeAndUser(Token.Type.REFRESH, user);

        Long ttl = jwtService.getExpiration(jwt);
        redisTemplate.opsForValue()
                .set(jwt, "logout", ttl, TimeUnit.MILLISECONDS);
    }
}

