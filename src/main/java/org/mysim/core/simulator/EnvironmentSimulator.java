package org.mysim.core.simulator;

import org.mysim.core.simulator.ai.EnvironmentSimulatorAI;

public class EnvironmentSimulator extends Simulator {

    public EnvironmentSimulator(String simulatorId) {
        super(simulatorId);
        simulatorAI = new EnvironmentSimulatorAI(this);
    }



}
