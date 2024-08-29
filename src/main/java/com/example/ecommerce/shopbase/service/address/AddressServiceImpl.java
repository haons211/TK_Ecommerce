package com.example.ecommerce.shopbase.service.address;

import com.example.ecommerce.shopbase.dto.PlaceDetail;
import com.example.ecommerce.shopbase.dto.response.AddressResponse;
import com.example.ecommerce.shopbase.entity.Address;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.mapper.AddressMapper;
import com.example.ecommerce.shopbase.repository.AddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {

    AddressRepository addressRepository;

    AddressMapper addressMapper;

    @Override
    public List<AddressResponse> getChildAddressList(Integer parent_id) {

        List<Address> addresses = addressRepository.findAllByParentId(parent_id);

        return addresses.stream().map(address -> {
            AddressResponse addressResponse = addressMapper.toAddressResponse(address);
            addressResponse.setType(address.getType().name());
            return addressResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public PlaceDetail getLocationDetail(Integer ward_id) {

        Address ward = addressRepository.findByIdAndType(ward_id, Address.Type.Ward)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        Address district = addressRepository.findById(ward.getParentId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        Address city = addressRepository.findById(district.getParentId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        return PlaceDetail.builder()
                .city(city.getName())
                .district(district.getName())
                .ward(ward.getName())
                .build();
    }
}
