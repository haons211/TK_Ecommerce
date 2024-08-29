package com.example.ecommerce.shopbase.mapper;

import com.example.ecommerce.shopbase.dto.response.inventory.SellerOrderItemResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.ViewOrderItemResponse;
import com.example.ecommerce.shopbase.entity.CartItemAttribute;
import com.example.ecommerce.shopbase.entity.OrderItem;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.repository.CartItemAttributeRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.service.product.ProductAssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemMapper {

    ProductAssetService productAssetService;
    CartItemAttributeRepository cartItemAttributeRepository;
    ValueCombinationRepository valueCombinationRepository;

    public  SellerOrderItemResponse toSellerOrderItemResponse(OrderItem item) {
        return SellerOrderItemResponse.builder()
                .buyer_name(item.getCartItem().getUser().getUsername())
                .ord_item_name(item.getCartItem().getProduct().getName())
                .ord_item_image(productAssetService.getImg(item.getCartItem().getProduct().getId()).get(0))
                .ord_item_price(item.getPrice())
                .ord_item_quantity(item.getOrd_item_quantity())
                .ord_item_status(item.getOrd_item_status())
                .ord_item_code(item.getOrd_item_code())
                .shp_provider(item.getSeller().getName())
                .build();
    }

    public ViewOrderItemResponse toViewOrderItemResponse(OrderItem item) {
        return ViewOrderItemResponse.builder()
                .ord_item_code(item.getOrd_item_code())
                .ord_item_image(productAssetService.getImg(item.getCartItem().getProduct().getId()).get(0))
                .ord_item_price(item.getPrice())
                .ord_item_quantity(item.getOrd_item_quantity())
                .ord_item_status(item.getOrd_item_status())
                .ord_item_product_value1(getValueNameByOrderItem(item).getValue1().getValue())
                .ord_item_product_value2(getValueNameByOrderItem(item).getValue2().getValue())
                .ord_item_name(item.getCartItem().getProduct().getName())
                .build();
    }

    public ValueCombination getValueNameByOrderItem(OrderItem item) {
        CartItemAttribute cartItemAttribute = cartItemAttributeRepository.findByCartItem(item.getCartItem());
        return cartItemAttribute.getValueCombination();
    }
}
