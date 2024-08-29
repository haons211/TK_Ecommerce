package com.example.ecommerce.shopbase.config.redis;

import com.example.ecommerce.shopbase.dto.OrderItemDTO;
import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.CartItemAttribute;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.CartItemAttributeRepository;
import com.example.ecommerce.shopbase.repository.CartItemRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    static String ORDER_KEY_PREFIX = "order:";

    private final RedisTemplate<Object, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemAttributeRepository cartItemAttributeRepository;
    private final ValueCombinationRepository valueCombinationRepository;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer,
                                      RedisTemplate<Object, Object> redisTemplate,
                                      ObjectMapper objectMapper,
                                      CartItemRepository cartItemRepository,
                                      CartItemAttributeRepository cartItemAttributeRepository,
                                      ValueCombinationRepository valueCombinationRepository) {
        super(listenerContainer);
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cartItemRepository = cartItemRepository;
        this.cartItemAttributeRepository = cartItemAttributeRepository;
        this.valueCombinationRepository = valueCombinationRepository;
    }

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if(expiredKey.startsWith(ORDER_KEY_PREFIX)) {
            Map<Object, Object> orderDetail = redisTemplate.opsForHash().entries("backup:" + expiredKey);

            if(orderDetail.isEmpty())
                throw new AppException(ErrorCode.NOT_FOUND);

            String orderItemJSON = (String) orderDetail.get("orderItems");
            try {
                List<OrderItemDTO> orderItems = objectMapper.readValue(orderItemJSON,
                        new TypeReference<List<OrderItemDTO>>() {});

                orderItems.forEach(orderItem -> {
                    CartItem cartItem = cartItemRepository.findById(orderItem.getCartItemId()).get();
                    CartItemAttribute cartItemAttribute = cartItemAttributeRepository.findByCartItem(cartItem);
                    ValueCombination valueCombination = cartItemAttribute.getValueCombination();
                    Integer oldQuantity = valueCombination.getQuantity();
                    valueCombination.setQuantity(oldQuantity + orderItem.getQuantity());
                    valueCombinationRepository.save(valueCombination);
                    redisTemplate.delete("backup:" + expiredKey);
                });

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
