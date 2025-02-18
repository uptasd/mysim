package org.mysim.core.simulator;

import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

public class ResourceSimulator extends Simulator {

    public ResourceSimulator(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        super(simulatorProperty, simulatorAI);
    }
    @Deprecated
    public ResourceSimulator(SimulatorProperty simulatorProperty) {
        super(simulatorProperty);
    }
    //用于测试
    @Deprecated
    public ResourceSimulator(String simulatorId) {
        super(simulatorId);
    }



}
