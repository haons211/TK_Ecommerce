package com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN;

import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GHNShippingRequest extends ShippingRequest {

    private Integer service_type_id;
    private Integer from_district_id;
    private Integer to_district_id;
    private String to_ward_code;
    private Integer height;
    private Integer length;
    private int weight;
    private Integer width;
    private Integer insurance_value;
    private String coupon;
    private Integer cod_failed_amount;
    private Integer cod_value;
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String name;
        private String code;
        private int quantity;
        private Integer height;
        private Integer weight;
        private Integer length;
        private Integer width;
    }
}
