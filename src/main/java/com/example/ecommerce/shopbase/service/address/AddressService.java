package com.example.ecommerce.shopbase.service.address;

import com.example.ecommerce.shopbase.dto.PlaceDetail;
import com.example.ecommerce.shopbase.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getChildAddressList(Integer parent_id);
    PlaceDetail getLocationDetail(Integer ward_id);
}
