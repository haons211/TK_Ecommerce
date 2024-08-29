package com.example.ecommerce.shopbase.service.user;


public interface OtpService {

    String getOTP(String email);

    boolean verifyOTP(String otp, String hashOtp);
}
