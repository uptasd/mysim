package org.mysim.core.simulator.status;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventSimulatorProperty extends SimulatorProperty {
    private int eventContinueTurn;
    private List<String> subscriberTypes;
    {
        this.setSimulatorType("EventPublisher");
    }

}
