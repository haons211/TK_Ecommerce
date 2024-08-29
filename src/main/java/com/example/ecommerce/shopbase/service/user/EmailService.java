package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
