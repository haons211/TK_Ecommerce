package com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ServicePackageRequest {
    private int shop_id;
    private int from_district;
    private int to_district;
}
