package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.constant.PredefinedRole;
import com.example.ecommerce.shopbase.dto.PlaceDetail;
import com.example.ecommerce.shopbase.dto.request.RegisterSellerRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateSellerRequest;
import com.example.ecommerce.shopbase.dto.response.SellerProfileResponse;
import com.example.ecommerce.shopbase.dto.response.SellerResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.SellerOrderItemResponse;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.mapper.OrderItemMapper;
import com.example.ecommerce.shopbase.mapper.SellerMapper;
import com.example.ecommerce.shopbase.repository.*;
import com.example.ecommerce.shopbase.service.address.AddressService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerServiceImpl implements SellerService {


    OrderItemMapper orderItemMapper;
    AddressService addressService;

    SellerRepository sellerRepository;

    AddressRepository addressRepository;

    SecurityUtils securityUtils;

    SellerMapper sellerMapper;

    RoleRepository roleRepository;

    UserRepository userRepository;

    OrderItemRepository orderItemRepository;

    OrderItemMapper mapper;

    @Override
    @Transactional
    public void register(RegisterSellerRequest request) {
        User user = securityUtils.getCurrentUserLogin();

        if(sellerRepository.existsByUserOrName(user, request.getName()))
            throw new AppException(ErrorCode.SELLER_EXISTED);

        Address address = addressRepository.findByIdAndType(request.getAddressId(), Address.Type.Ward)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));

        Seller seller = sellerMapper.toSeller(request);
        seller.setUser(user);
        seller.setAddress(address);

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        roleRepository.findById(PredefinedRole.SELLER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        userRepository.save(user);
        sellerRepository.save(seller);
    }

    @Override
    public SellerResponse getInfo(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        Integer wardId = seller.getAddress().getId();
        PlaceDetail place = addressService.getLocationDetail(wardId);

        SellerResponse sellerResponse = sellerMapper.toSellerResponse(seller);
        sellerResponse.setPlace(place);

        return sellerResponse;
    }

    @Override
    @Transactional
    @PostAuthorize("returnObject.accountName == authentication.name")
    public SellerResponse updateSeller(Integer sellerId, UpdateSellerRequest request) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        sellerMapper.updateSeller(seller, request);
        seller.setUpdatedAt(new Date());

        return sellerMapper.toSellerResponse(sellerRepository.save(seller));

    }

    @Override
    public Integer getWardId(Integer sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));
        return seller.getAddress().getParentId();
        }

    @Override
    public SellerProfileResponse getSellerProfile() {
        User currentUser = securityUtils.getCurrentUserLogin();

        Seller seller = sellerRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        return sellerMapper.toSellerProfileResponse(seller);
    }

    @Override
    public List<SellerOrderItemResponse> getOrderItemResponse(String status) {
        User currentUser = securityUtils.getCurrentUserLogin();

        Seller seller = sellerRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        List<OrderItem> listOrderItems = orderItemRepository.findBySeller(seller.getId());
        List<SellerOrderItemResponse> responseList = new ArrayList<>();
        if(status.equals("ALL")){
            responseList = listOrderItems.stream()
                    .map(orderItemMapper::toSellerOrderItemResponse) // Use method reference
                    .collect(Collectors.toList());} else if (status.equals("SUCCESSFUL")) {
            List<OrderItem> listOrderItems_Success = orderItemRepository.findByStatus(OrderItem.Status.SUCCESSFUL);
            responseList = listOrderItems.stream()
                    .map(orderItemMapper::toSellerOrderItemResponse) // Use method reference
                    .collect(Collectors.toList());
            }
         else if (status.equals("FAILED")) {
            List<OrderItem> listOrderItems_Failded = orderItemRepository.findByStatus(OrderItem.Status.FAILED);
            responseList = listOrderItems.stream()
                    .map(orderItemMapper::toSellerOrderItemResponse) // Use method reference
                    .collect(Collectors.toList());

        }

        return responseList;
    }

}



