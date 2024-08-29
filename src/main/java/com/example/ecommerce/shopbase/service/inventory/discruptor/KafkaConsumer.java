package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaConsumer {

    ExecutorService executorService = Executors.newFixedThreadPool(5, DaemonThreadFactory.INSTANCE);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RingBufferService ringBufferService;

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    ValueCombinationRepository valueCombinationRepository;

    @Autowired
    RedisTemplate redisTemplate;


    @KafkaListener(topics = "i_command", groupId = "ghtk-Group")
    public void consumeMessage(CommandEventData eventData) throws JsonProcessingException {
        //System.out.println("Consumed message: " + message);
        long sequence = ringBufferService.ringBuffer.next();
        //CommandEventData eventData = objectMapper.readValue(message, CommandEventData.class);
        List<CombinationValueCommand> commands = eventData.getCommand();
        executorService.submit(() -> {
            CommandEventData event = ringBufferService.ringBuffer.get(sequence);
            commands.forEach(command -> {
                Integer quantity = (Integer) baseRedisService.get(command.getCombinationId().toString());
                if(quantity == null){
                    ValueCombination valueCombinationRedis = valueCombinationRepository.findById(command.getCombinationId()).orElse(null);
                    redisTemplate.opsForValue().set(valueCombinationRedis.getId().toString(), valueCombinationRedis.getQuantity(), 1, java.util.concurrent.TimeUnit.MINUTES);
                    Boolean result = redisTemplate.expire(valueCombinationRedis.getId().toString(), 1, java.util.concurrent.TimeUnit.MINUTES);
                    //baseRedisService.set(valueCombinationRedis.getId().toString(), valueCombinationRedis.getQuantity());
                    //redisTemplate.expire(valueCombinationRedis.getId().toString(), 1, java.util.concurrent.TimeUnit.MINUTES);
                    //baseRedisService.setTimeToLiveInMinutes(valueCombinationRedis.getId().toString(), 1);
                }
            });
            event.setId(eventData.getId());
            event.setCommand(commands);
            ringBufferService.ringBuffer.publish(sequence);
        });
    }
}
