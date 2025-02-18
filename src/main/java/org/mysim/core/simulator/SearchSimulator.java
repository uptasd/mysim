package org.mysim.core.simulator;

import org.mysim.core.simulator.ai.SearchSimulatorAI;

public class SearchSimulator extends Simulator {


    public SearchSimulator(String simulatorId) {
        super(simulatorId);
        simulatorAI = new SearchSimulatorAI(this);
        simulatorAI.setSimulator(this);
    }
}
