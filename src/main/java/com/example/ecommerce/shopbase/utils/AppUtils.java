package com.example.ecommerce.shopbase.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class AppUtils {
    public static String generateOtp() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        int count = 0;

        while(count < 4) {
            otp.append(random.nextInt(10));
            ++count;
        }

        return otp.toString();
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
