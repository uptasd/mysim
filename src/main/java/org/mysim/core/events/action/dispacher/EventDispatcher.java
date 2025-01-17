package org.mysim.core.events.action.dispacher;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.SimulationActor;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventDispatcher extends SimulationActor {
//    Map<String, SimulationActor> actors = new HashMap<>();
    //本质是个转发器，不需要区分action和execute
    public EventDispatcher(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void action(SimMessage simMessage) {
        buildContext(simMessage);
        execute(context);
    }

    public abstract List<String> getAllActors();


}
