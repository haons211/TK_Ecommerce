package com.example.ecommerce.shopbase.dto.inventory;

import com.lmax.disruptor.EventFactory;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommandEventData {
    private String id;

    private List<CombinationValueCommand> command;

    public static final EventFactory<CommandEventData> EVENT_FACTORY = new EventFactory<CommandEventData>() {
        @Override
        public CommandEventData newInstance() {
            return new CommandEventData();
        }
    };

}
