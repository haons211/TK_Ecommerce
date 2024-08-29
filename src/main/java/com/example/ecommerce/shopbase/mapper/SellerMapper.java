package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.request.RegisterSellerRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateSellerRequest;
import com.example.ecommerce.shopbase.dto.response.SellerProfileResponse;
import com.example.ecommerce.shopbase.dto.response.SellerResponse;
import com.example.ecommerce.shopbase.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    Seller toSeller(RegisterSellerRequest request);

    @Mapping(target = "avatar", source = "imagePath")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "sellerId", source = "id")
    @Mapping(target = "accountName", source = "user.username")
    SellerResponse toSellerResponse(Seller seller);

    @Mapping(target = "sellerId", source = "id")
    SellerProfileResponse toSellerProfileResponse(Seller seller);

    void updateSeller(@MappingTarget Seller seller, UpdateSellerRequest request);
}
