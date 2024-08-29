package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadFactory;

@Service
public class RingBufferService {

    ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
    WaitStrategy waitStrategy;
    Disruptor<CommandEventData> disruptor;
    RingBuffer<CommandEventData> ringBuffer;

    @Autowired
    EventConsumerProcess eventConsumerProcess;

    @PostConstruct
    public void init() {
        waitStrategy = new YieldingWaitStrategy();
        Disruptor<CommandEventData> disruptor
                = new Disruptor<>(
                CommandEventData.EVENT_FACTORY,
                1024,
                threadFactory,
                ProducerType.MULTI,
                waitStrategy);

        disruptor.handleEventsWith(eventConsumerProcess);
        ringBuffer = disruptor.start();
    }

    public void shutdown(){
        this.disruptor.shutdown();
    }

}
