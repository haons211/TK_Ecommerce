package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.utils.AppUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {

    PasswordEncoder passwordEncoder;

    RedisTemplate<Object, Object> redisTemplate;

    @NonFinal
    @Value("${otp.time.expiration}")
    Long OTP_TIME_EXPIRATION;

    @Transactional
    public String getOTP(String email) {
        redisTemplate.opsForValue().getAndDelete(email);

        String otp = AppUtils.generateOtp();

        redisTemplate.opsForValue().set(
                email,
                passwordEncoder.encode(otp),
                OTP_TIME_EXPIRATION,
                TimeUnit.MILLISECONDS
        );

        return otp;
    }

    @Override
    public boolean verifyOTP(String otp, String hashOtp) {
        if(hashOtp == null)
            return false;

        return passwordEncoder.matches(otp, hashOtp);
    }
}
