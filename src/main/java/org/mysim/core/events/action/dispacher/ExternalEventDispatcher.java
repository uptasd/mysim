package org.mysim.core.events.action.dispacher;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class ExternalEventDispatcher extends EventDispatcher{
    Map<String, ExternalEventActor> actors = new HashMap<>();
    public ExternalEventDispatcher(SimulatorAI simulatorAI) {
        super(simulatorAI);
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
        ExternalEventActor actor = actors.getOrDefault(eventName, null);
        if (actor == null) {
            log.debug("{} cant handle system event:{}", property.getSimulatorId(), eventName);
            return;
        }
        actor.action(eventMessage);
    }
    public void registerExternalEventActor(String actionName, ExternalEventActor actor) {
        actors.put(actionName, actor);
    }
}
