package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.simulator.ai.SimulatorAI;

public class Step extends SystemActor {
    public Step(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }


    @Override
    public void execute(ActionContext context) {
        simulatorAI.step();
    }


}
