package com.example.ecommerce.shopbase.service.OrderItem;

import com.example.ecommerce.shopbase.dto.OrderItemDTO;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.CartItemRepository;
import com.example.ecommerce.shopbase.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {
  OrderItemRepository orderItemRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public void setCartItemIdForDelete(int cartItemId) {
    return ;
  }


//  @Override
//  @Transactional
//  public void setCartItemIdForDelete(int cartItemId) {
//    List<OrderItem> ordersToDelete = orderItemRepository.findAllByCartItemId(cartItemId);
//    List<OrderItemDTO> ordersToUpdate = new ArrayList<>();
//
//    for (OrderItem orderItem : ordersToDelete) {
//      OrderItemDTO orderItemDTO = OrderItemDTO.builder()
//          .cartItemId(0)
//          .price(orderItem.getPrice())
//          .quantity(orderItem.getOrd_item_quantity())
//          .build();
//      ordersToUpdate.add(orderItemDTO);
//    }
//
//
//    }


//  @Override
//  @Transactional
//  public void updateCartItemInOrderItem(CartItem cartItemId) {
//    List<OrderItem> orderItemList = orderItemRepository.findAllByCartItemId(cartItemId);
//    for (OrderItem orderItem : orderItemList) {
//      OrderItem orderItem = OrderItem.builder()
//          .cartItem(cartItemRepository.getReferenceById(cartItemId)
//
//            orderItem.getPrice())
//          .quantity(orderItem.getOrd_item_quantity())
//          .build();
//      ordersToUpdate.add(orderItemDTO);
//    }
//

//    }
//    return  orderItemRepository.saveAll(ordersToUpdate);
//  }

}


