package com.example.ecommerce.shopbase.service.inventory.discruptor;

import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.dto.inventory.PersistDBCommand;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadFactory;

@Service
public class RingBufferPersistDBService {

    ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
    WaitStrategy waitStrategy;
    Disruptor<PersistDBCommand> disruptor;
    RingBuffer<PersistDBCommand> ringBuffer;

    @Autowired
    EventPersistDBProcess eventPersistDBProcess;

    @PostConstruct
    public void init() {
        waitStrategy = new BlockingWaitStrategy();
        Disruptor<PersistDBCommand> disruptor
                = new Disruptor<>(
                PersistDBCommand.EVENT_FACTORY,
                1024,
                threadFactory,
                ProducerType.SINGLE,
                waitStrategy);

        disruptor.handleEventsWith(eventPersistDBProcess);
        ringBuffer = disruptor.start();
    }

    public void shutdown(){
        this.disruptor.shutdown();
    }

}
