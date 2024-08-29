package com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN;

import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GHNShippingResponse extends ShippingResponse {
    private int code;
    private String message;
    private Datas data;

    @Data
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Datas {
        private int total; // Tổng tiền dịch vụ
        private int serviceFee; // Phí dịch vụ
        private int insuranceFee; // Phí khai giá hàng hóa
        private int pickStationFee; // Phí gửi hàng tại bưu cục
        private int couponValue; // Giá trị khuyến mãi
        private int r2sFee; // Phí giao lại hàng
        private int returnAgain; // Phí giao lại hàng
        private int documentReturn; // Phí giao tài liệu
        private int doubleCheck; // Phí đồng kiểm
        private int codFee; // Phí thu tiền COD
        private int pickRemoteAreasFee; // Phí lấy hàng vùng xa
        private int deliverRemoteAreasFee; // Phí giao hàng vùng xa
        private int codFailedFee; // Phí thu tiền khi giao thất bại
    }
}
