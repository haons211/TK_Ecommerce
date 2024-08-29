package com.example.ecommerce.shopbase.service.shipping;

import com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN.GHNShippingRequest;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingRequest;
import com.example.ecommerce.shopbase.entity.Address;
import com.example.ecommerce.shopbase.entity.AddressShipping;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.AddressRepository;
import com.example.ecommerce.shopbase.repository.AddressShippingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShippingRequestFactory {

    private final AddressShippingRepository addressShippingRepository;
    private final AddressRepository addressRepository;

    public ShippingRequestFactory(AddressShippingRepository addressShippingRepository,
                                  AddressRepository addressRepository) {
        this.addressShippingRepository = addressShippingRepository;
        this.addressRepository = addressRepository;
    }

    public ShippingRequest createRequest(String serviceType, Map<String, Object> params) {
        switch (serviceType) {
            case "GHN":
                return buildGHNShippingRequest(params);
            default:
                throw new IllegalArgumentException("Unknown service type: " + serviceType);
        }
    }



    private GHNShippingRequest buildGHNShippingRequest(Map<String, Object> params) {
        Address address = addressRepository.findById((Integer) params.get("to_district_id"))
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        AddressShipping addressShipping = addressShippingRepository.findByAddress(address);
        return GHNShippingRequest.builder()
                .service_type_id(0)
                .from_district_id(0)
                .to_district_id(Integer.parseInt(addressShipping.getAddressShippingCode()))
                .to_ward_code(null)
                .height(0)
                .length(0)
                .weight(0)
                .width(0)
                .insurance_value(0)
                .coupon(null)
                .cod_failed_amount(0)
                .cod_value(0)
                .items(null)
                .build();
    }



}
