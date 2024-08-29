package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.dto.PlaceDetail;
import com.example.ecommerce.shopbase.dto.request.CreateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserAddressRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateUserRequest;
import com.example.ecommerce.shopbase.dto.response.UserAddressResponse;
import com.example.ecommerce.shopbase.dto.response.UserResponse;
import com.example.ecommerce.shopbase.entity.Address;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.entity.UserAddress;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.mapper.UserAddressMapper;
import com.example.ecommerce.shopbase.mapper.UserMapper;
import com.example.ecommerce.shopbase.repository.AddressRepository;
import com.example.ecommerce.shopbase.repository.UserAddressRepository;
import com.example.ecommerce.shopbase.repository.UserRepository;
import com.example.ecommerce.shopbase.service.address.AddressService;
import com.example.ecommerce.shopbase.utils.AppUtils;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.sql.Update;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    AddressService addressService;

    AddressRepository addressRepository;

    UserAddressRepository userAddressRepository;

    UserMapper userMapper;

    UserAddressMapper userAddressMapper;

    SecurityUtils securityUtils;

    @Override
    public UserResponse getMyInfo() {
        return userMapper.toUserResponse(securityUtils.getCurrentUserLogin());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
        User currentUser = securityUtils.getCurrentUserLogin();

        if(!Objects.equals(currentUser.getId(), userId))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setUpdatedDateTime(new Date());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void createUserAddress(CreateUserAddressRequest request) {
        User user = securityUtils.getCurrentUserLogin();
        Address ward = addressRepository.findByIdAndType(request.getWardId(), Address.Type.Ward)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        UserAddress userAddress = userAddressMapper.toUserAddress(request);
        userAddress.setUser(user);
        userAddress.setAddress(ward);

        boolean isDefault = userAddressRepository.findAllByUser(user).size() == 0;
        userAddress.setIsDefault(isDefault);

        userAddressRepository.save(userAddress);
    }

    @Override
    public List<UserAddressResponse> getUserAddressList() {
        User user = securityUtils.getCurrentUserLogin();

        List<UserAddress> userAddresses = userAddressRepository.findAllByUser(user);

        return userAddresses.stream().map(userAddress -> mapToUserAddressResponse(userAddress, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserAddress(UpdateUserAddressRequest request) {
        UserAddress userAddress = userAddressRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        Address newAddress = addressRepository.findByIdAndType(request.getWardId(), Address.Type.Ward)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        userAddressMapper.updateUserAddress(userAddress, request);
        userAddress.setAddress(newAddress);

        userAddressRepository.save(userAddress);
    }

    @Override
    @Transactional
    public void deleteUserAddress(Integer addressId) {
        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        if(userAddress.getIsDefault())
            throw new AppException(ErrorCode.ADDRESS_DEFAULT);

        userAddressRepository.delete(userAddress);
    }

    @Override
    @Transactional
    public void setAddressDefault(Integer addressId) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        userAddressRepository.findDefaultAddress(address.getUser())
                .ifPresent(oldDefaultAddress -> oldDefaultAddress.setIsDefault(false));
        address.setIsDefault(true);

        userAddressRepository.save(address);
    }

    public UserAddressResponse mapToUserAddressResponse(UserAddress userAddress, Integer userId) {
            Address ward = userAddress.getAddress();

            PlaceDetail placeDetail = addressService.getLocationDetail(ward.getId());

            UserAddressResponse userAddressResponse = userAddressMapper.toUserAddressResponse(userAddress);
            userAddressResponse.setUserId(userId);
            userAddressResponse.setCity(placeDetail.getCity());
            userAddressResponse.setDistrict(placeDetail.getDistrict());
            userAddressResponse.setWard(placeDetail.getWard());

            return userAddressResponse;
    }
}
