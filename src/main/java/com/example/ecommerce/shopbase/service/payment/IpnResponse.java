package com.example.ecommerce.shopbase.service.payment;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IpnResponse {

    @JsonProperty("RspCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;
}
