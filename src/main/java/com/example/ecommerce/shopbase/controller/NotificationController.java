package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.event.NotificationEvent;
import com.example.ecommerce.shopbase.dto.EmailDetails;
import com.example.ecommerce.shopbase.service.user.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    EmailService emailService;

    @KafkaListener(topics = "notification-delivery", groupId = "ghtk-Group2")
    public void listenNotificationDelivery(NotificationEvent message) {

        emailService.sendEmail(EmailDetails.builder()
                        .subject(message.getSubject())
                        .recipient(message.getRecipient())
                        .messageBody(message.getBody())
                .build());

        log.info("Message received: {}", message);
    }

}
