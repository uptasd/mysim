package org.mysim.core.events.action.system;

import org.mysim.core.events.action.SimulationActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;

public abstract class SystemActor extends SimulationActor {
    public SystemActor(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void action(SimMessage simMessage) {
        buildContext(simMessage);
        execute(context);
    }



}
