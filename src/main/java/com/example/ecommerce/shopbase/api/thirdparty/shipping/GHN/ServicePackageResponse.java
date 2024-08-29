package com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN;

import lombok.Data;
import java.util.List;
@Data
public class ServicePackageResponse {
    List<ServicePackage> data;
    @Data
    public static class ServicePackage {
        private int service_id;
        private String short_name;
        private int service_type_id;
        private String config_fee_id;
        private String extra_cost_if;
        private String standard_config_fee_id;
        private String standart_extra_cost_id;
    }
}