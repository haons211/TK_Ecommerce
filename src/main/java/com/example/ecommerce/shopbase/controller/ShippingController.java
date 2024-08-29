package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.ShippingFeeResponse;
import com.example.ecommerce.shopbase.service.order.OrderService;
import com.example.ecommerce.shopbase.service.shipping.ShippingRequestFactory;
import com.example.ecommerce.shopbase.service.shipping.ShippingService;
import com.example.ecommerce.shopbase.service.shipping.ShippingServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/shipping")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingController {

    OrderService orderService;
    ShippingServiceFactory shippingServiceFactory;
    ShippingRequestFactory shippingRequestFactory;


    @PostMapping("/totalcost")
    public ApiResponse<ShippingFeeResponse> getTotalCost(@RequestParam("shippingContribute") String shippingContribute,
                                                         @RequestParam("orderId") String orderId,
                                                         @RequestBody Map<String,Object> params) throws JsonProcessingException {
        ShippingRequest request = shippingRequestFactory.createRequest(shippingContribute,params);
        ShippingService shippingService = shippingServiceFactory.getShippingService(shippingContribute);
        Integer totalCost = shippingService.getTotalFee(request,orderId);
        Integer realCost = orderService.getRealPrice(orderService.getPreOrderDetail((orderId)));
        ShippingFeeResponse result= ShippingFeeResponse.builder()
                .total_cost(totalCost)
                .shipping_fee(totalCost-realCost).build();
        return ApiResponse.<ShippingFeeResponse>builder()
                .result(result)
                .build();
    }
   
}
