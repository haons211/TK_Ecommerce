package com.example.ecommerce.shopbase.api.thirdparty.payment.VNPay;

import com.example.ecommerce.shopbase.api.thirdparty.payment.PaymentRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VNPayPaymentRequest extends PaymentRequest {
    private String vnp_version;
    private String vnp_command;
    private String vnp_tmn_code;
    private Long vnp_amount;
    private String vnp_bank_code;
    private String vnp_create_date;
    private String vnp_curr_code;
    private String vnp_ip_addr;
    private String vnp_locale;
    private String vnp_order_info;
    private String vnp_order_type;
    private String vnp_return_url;
    private String vnp_expire_date;
    private String vnp_txn_ref;
    private String vnp_secure_hash;
}
