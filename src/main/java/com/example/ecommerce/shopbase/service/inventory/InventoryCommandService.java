package com.example.ecommerce.shopbase.service.inventory;

import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryCommandService {

    @Autowired
    KafkaTemplate<String, CommandEventData> kafkaTemplate;

    public void receiveCommand(CommandEventData data){
        kafkaTemplate.send("i_command",data);
    }

}
