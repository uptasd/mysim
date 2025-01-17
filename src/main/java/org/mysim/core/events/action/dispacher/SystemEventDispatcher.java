package org.mysim.core.events.action.dispacher;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.*;
import org.mysim.core.events.action.system.Step;
import org.mysim.core.events.action.system.SyncTurn;
import org.mysim.core.events.action.system.SystemActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class SystemEventDispatcher extends EventDispatcher {
    private final HashMap<String, SimulationActor> actors = new HashMap<>();

    public SystemEventDispatcher(SimulatorAI simulatorAI) {
        super(simulatorAI);
        actors.put(SystemEvents.STEP.name(), new Step(simulatorAI));
        actors.put(SystemEvents.SYNC_TURN.name(), new SyncTurn(simulatorAI));
    }

    @Override
    public List<String> getAllActors() {
        return actors.keySet().stream().toList();
    }


    @Override
    public void execute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        SimMessage eventMessage = getSimMessage();
        String eventName = eventMessage.getEventName();
        SimulationActor actor = actors.getOrDefault(eventName, null);
        if (actor == null) {
            log.debug("{} cant handle system event:{}", property.getSimulatorId(), eventName);
            return;
        }
        actor.action(eventMessage);
    }

    public void registerSystemEvent(String name, SystemActor systemActor) {
        actors.put(name, systemActor);
    }

}
