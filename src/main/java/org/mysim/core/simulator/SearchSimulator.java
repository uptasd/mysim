package org.mysim.core.simulator;

import lombok.Data;
import lombok.Getter;
import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.system.SystemActor;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.ai.SearchSimulatorAI;

public class SearchSimulator extends Simulator {


    public SearchSimulator(String simulatorId) {
        super(simulatorId);
        simulatorAI = new SearchSimulatorAI(this);
        simulatorAI.setSimulator(this);
    }
}
