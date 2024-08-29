package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.dto.inventory.PersistDBCommand;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPersistDBProcess implements EventHandler<PersistDBCommand> {

    @Autowired
    ValueCombinationRepository valueCombinationRepository;

    @Autowired
    KafkaTemplate<String,PersistDBCommand> kafkaTemplate;

    @Override
    public void onEvent(PersistDBCommand persistDBCommand, long sequence, boolean endOfBatch) {
        valueCombinationRepository.updateQuantityById(persistDBCommand.getCombinationValueRedisDTO().getQuantity(), persistDBCommand.getCombinationValueRedisDTO().getId());
    }
}