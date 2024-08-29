package com.example.ecommerce.shopbase.service.shipping;

import com.example.ecommerce.shopbase.service.shipping.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
public class ShippingServiceFactory {

    @Autowired
    private Map<String, ShippingService> shippingServices;

    public ShippingService getShippingService(String serviceType) {
        ShippingService service = shippingServices.get(serviceType);
        if (service == null) {
            throw new IllegalArgumentException("No such shipping service: " + serviceType);
        }
        return service;
    }
}
