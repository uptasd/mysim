package org.mysim.quickstart.simulators.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.status.SimulatorProperty;
@Slf4j
public class RestaurantAI extends ResourceAI {
    @Override
    public void step() {
        super.step();
        SimulatorProperty property = getSimulatorProperty();
        log.info("{}:step,current turn={}", property.getSimulatorId(), property.getTurn());

    }
}
