package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.dto.inventory.CombinationValueRedisDTO;
import com.example.ecommerce.shopbase.dto.inventory.InventoryResult;
import com.example.ecommerce.shopbase.dto.inventory.PersistDBCommand;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProcessService {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    RingBufferPersistDBService rB;

    @Autowired
    KafkaTemplate<String, InventoryResult> kafkaTemplate;

    public boolean process(List<CombinationValueCommand> commands) {
        AtomicBoolean resultCheck = new AtomicBoolean(true);

        commands.forEach(command->{
            Integer quantity = baseRedisService.getInventoryQuantity(command.getCombinationId().toString());
            if(quantity == null){
                resultCheck.set(false);
            }
            else{
                if( command.getChangeNumber() <= 0){
                    if(quantity < Math.abs(command.getChangeNumber()) || quantity <=0){
                        resultCheck.set(false);
                    }
                }
            }
        });

        if(resultCheck.get()==false) return  false;

        commands.forEach(command -> {
            Integer quantity = baseRedisService.getInventoryQuantity(command.getCombinationId().toString());

            Integer newQuantity = quantity + command.getChangeNumber();
            baseRedisService.set(command.getCombinationId().toString(),newQuantity);
            long sequenceId = rB.ringBuffer.next();
            PersistDBCommand persistDBCommand = rB.ringBuffer.get(sequenceId);
            persistDBCommand.setCombinationValueRedisDTO(new CombinationValueRedisDTO(command.getCombinationId(),newQuantity));
            rB.ringBuffer.publish(sequenceId);
        });

        return true;
    }

}
