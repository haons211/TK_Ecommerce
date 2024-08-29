package com.example.ecommerce.shopbase.service.order;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.OrderItemDTO;
import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.dto.inventory.InventoryResult;
import com.example.ecommerce.shopbase.dto.request.CreatePreOrderRequest;
import com.example.ecommerce.shopbase.dto.response.PreOrderResponse;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.*;
import com.example.ecommerce.shopbase.service.product.InventoryService;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    SecurityUtils securityUtils;
    BaseRedisService redisService;
    InventoryService inventoryService;
    CartItemAttributeRepository cartItemAttributeRepository;
    CartItemRepository cartItemRepository;
    ObjectMapper objectMapper;
    RedisTemplate<Object, Object> redisTemplate;

    static String ORDER_KEY_PREFIX = "order:";
    static String ORDER_KEY_BACKUP_PREFIX = "backup:";

    @Override
    public String checkOrderStatus(String orderId) {
        String key = ORDER_KEY_PREFIX + orderId;
        return (String) redisService.hashGet(key, "status");
    }

    @Override
    public PreOrderResponse createPreOrder(CreatePreOrderRequest request) throws JsonProcessingException {
        var currentUser = securityUtils.getCurrentUserLogin();
        List<Integer> items = request.getCartItemIds();

        List<OrderItemDTO> orderItems = items.stream().map(id -> {
            CartItem cartItem = cartItemRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

            CartItemAttribute cartItemAttribute = cartItemAttributeRepository.findByCartItem(cartItem);

            Integer price = cartItemAttribute.getValueCombination().getPrice();

            return OrderItemDTO.builder()
                    .cartItemId(id)
                    .quantity(cartItem.getQuantity())
                    .price(price)
                    .build();
        }).toList();

        String orderId = UUID.randomUUID().toString();
        String key = ORDER_KEY_PREFIX + orderId;
        String keyBackup = ORDER_KEY_BACKUP_PREFIX + key;
        String orderItemJSON = objectMapper.writeValueAsString(orderItems);

        Map<String, Object> orderDetail = new HashMap<>();
        orderDetail.put("userId", currentUser.getId());
        orderDetail.put("status", "pending");
        orderDetail.put("orderItems", orderItemJSON);


        redisTemplate.opsForHash().putAll(key, orderDetail);
        redisTemplate.opsForHash().putAll(keyBackup, orderDetail);
        redisTemplate.expire(key, 5 * 60, TimeUnit.SECONDS);
        redisTemplate.expire(keyBackup, 15 * 60, TimeUnit.SECONDS);

        List<CartItem> cartItems = cartItemRepository.findAllById(items);
        List<CombinationValueCommand> commands = new ArrayList<>();

        cartItems.forEach(item -> {
            CartItemAttribute cartItemAttribute = cartItemAttributeRepository.findByCartItem(item);
            commands.add(
                    CombinationValueCommand.builder()
                    .combinationId(cartItemAttribute.getValueCombination().getId())
                    .changeNumber(item.getQuantity() * -1)
                    .build());
        });

        inventoryService.reduceQuantity(orderId, commands);

        return PreOrderResponse.builder()
                .orderId(orderId)
                .build();
    }

    @Override
    public void updateOrderStatus(InventoryResult message) {
        String result = message.getResult() ? "Success" : "Failed";
        String key = ORDER_KEY_PREFIX + message.getId();
        redisService.hashSet(key, "status", result);
    }

    @Override
    public List<CartItemDetails> getPreOrderDetail(String orderId) throws JsonProcessingException {
        User currentUser = securityUtils.getCurrentUserLogin();
        String key = ORDER_KEY_PREFIX + orderId;

        Map<Object, Object> orderDetail = redisTemplate.opsForHash().entries(key);

        if(orderDetail.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND);

        String orderItemJSON = (String) orderDetail.get("orderItems");
        Integer userId = (Integer) orderDetail.get("userId");

        if(!Objects.equals(userId, currentUser.getId()))
            throw new AppException(ErrorCode.UNAUTHORIZED);
        List<OrderItemDTO> orderItemDTOS = objectMapper.readValue(orderItemJSON, new TypeReference<List<OrderItemDTO>>() {});

        return orderItemDTOS.stream().map(orderItem -> {
            CartItem cartItem = cartItemRepository.findById(orderItem.getCartItemId()).get();
            CartItemAttribute cartItemAttribute = cartItemAttributeRepository.findByCartItem(cartItem);
            ValueCombination valueCombination = cartItemAttribute.getValueCombination();
            Value value1 = valueCombination.getValue1();
            Value value2 = valueCombination.getValue2();

            CartItemDetails.ValueCombinations valueCombinationRepository = CartItemDetails.ValueCombinations.builder()
                    .value1_id(value1.getId())
                    .value1_name(value1.getValue())
                    .value2_id(value2.getId())
                    .value2_name(value2.getValue())
                    .build();

            return CartItemDetails.builder()
                    .id(cartItem.getId())
                    .name(cartItem.getProduct().getName())
                    .productId(cartItem.getProduct().getId())
                    .quantity(orderItem.getQuantity())
                    .sellerId(cartItem.getProduct().getSeller().getId())
                    .weight(cartItem.getProduct().getWeight())
                    .price(orderItem.getPrice())
                    .valueCombinationDetails(valueCombinationRepository)
                    .build();
        }).toList();
    }

    @Override
    public int getRealPrice(List<CartItemDetails> items) {
        int total = 0;

        for (CartItemDetails item : items) {
            // Simply use the price from CartItemDetails
            int itemPrice = item.getPrice();
            int itemQuantity = item.getQuantity();

            total += itemPrice * itemQuantity;
        }

        return total;
    }



}
