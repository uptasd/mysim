package org.mysim.quickstart.simulators.restaurant;

import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

public class Restaurant extends ResourceSimulator {
    public Restaurant(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        super(simulatorProperty, simulatorAI);
    }
}
