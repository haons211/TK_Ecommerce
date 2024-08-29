package com.example.ecommerce.shopbase.service.payment.constant;

import com.example.ecommerce.shopbase.service.payment.IpnResponse;

public class VnpIpnResponseConst {

    public static final IpnResponse SUCCESS = new IpnResponse("00", "Successful");
    public static final IpnResponse SUSPICIOUS_TRANSACTION = new IpnResponse("07", "Suspicious transaction (possible fraud)");
    public static final IpnResponse NOT_REGISTERED_FOR_INTERNET_BANKING = new IpnResponse("09", "Card/Account not registered for InternetBanking");
    public static final IpnResponse AUTHENTICATION_FAILED = new IpnResponse("10", "Authentication failed (more than 3 incorrect attempts)");
    public static final IpnResponse PAYMENT_TIMEOUT = new IpnResponse("11", "Payment timeout");
    public static final IpnResponse ACCOUNT_LOCKED = new IpnResponse("12", "Card/Account is locked");
    public static final IpnResponse INCORRECT_OTP = new IpnResponse("13", "Incorrect OTP, please retry");
    public static final IpnResponse TRANSACTION_CANCELLED_BY_CUSTOMER = new IpnResponse("24", "Transaction cancelled by customer");
    public static final IpnResponse INSUFFICIENT_FUNDS = new IpnResponse("51", "Insufficient funds in account");
    public static final IpnResponse DAILY_LIMIT_EXCEEDED = new IpnResponse("65", "Daily transaction limit exceeded");
    public static final IpnResponse BANK_UNDER_MAINTENANCE = new IpnResponse("75", "Bank is under maintenance");
    public static final IpnResponse INCORRECT_PAYMENT_PASSWORD = new IpnResponse("79", "Incorrect payment password, please retry");
    public static final IpnResponse UNKNOWN_ERROR = new IpnResponse("99", "Unknown error");
    public static final IpnResponse SIGNATURE_FAILED = new IpnResponse("97", "Signature failed");
    public static final IpnResponse ORDER_NOT_FOUND = new IpnResponse("01", "Order not found");

    // Add additional response codes as needed
}
