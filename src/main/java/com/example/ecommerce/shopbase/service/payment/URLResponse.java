package com.example.ecommerce.shopbase.service.payment;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class URLResponse {
    public String url;
    public String order_code;

}
