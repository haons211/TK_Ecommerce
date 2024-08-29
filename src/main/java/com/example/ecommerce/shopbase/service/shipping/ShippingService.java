package com.example.ecommerce.shopbase.service.shipping;


import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingRequest;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingResponse;
import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.response.ShippingFeeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ShippingService {
    int getTotalFee(ShippingRequest request,String orderId) throws JsonProcessingException;
    ShippingFeeResponse returnShippingFeeResponse(ShippingRequest request,String orderId) throws JsonProcessingException;
}
