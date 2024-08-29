package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.dto.inventory.InventoryResult;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventConsumerProcess implements EventHandler<CommandEventData> {

    @Autowired
    ProcessService processService;

    @Autowired
    KafkaTemplate<String, InventoryResult> kafkaTemplate;

    @Autowired
    RingBufferPersistDBService rB;

    @Override
    public void onEvent(CommandEventData eventData, long sequence, boolean endOfBatch) {
        List<CombinationValueCommand> commands = eventData.getCommand();
        boolean c = processService.process(commands);
        if(c){
            InventoryResult inventoryResult = new InventoryResult(eventData.getId(),true);
            kafkaTemplate.send("inventory_result",inventoryResult);
        }else{
            InventoryResult inventoryResult = new InventoryResult(eventData.getId(),false);
            kafkaTemplate.send("inventory_result",inventoryResult);
        }
    }
}