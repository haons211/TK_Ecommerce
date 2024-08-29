package com.example.ecommerce.shopbase.dto.inventory;

import com.lmax.disruptor.EventFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistDBCommand {
    private long id;

    private CombinationValueRedisDTO combinationValueRedisDTO;

    public static final EventFactory<PersistDBCommand> EVENT_FACTORY = new EventFactory<PersistDBCommand>() {
        @Override
        public PersistDBCommand newInstance() {
            return new PersistDBCommand();
        }
    };
}
