package org.mysim.core.events.action.dispacher;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.config.ActionFactory;
import org.mysim.core.simulator.config.ActionInfo;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.task.BPMNTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class BPMNEventDispatcher extends EventDispatcher {
    private final HashMap<String, ActionInfo> actors = new HashMap<>();

    public BPMNEventDispatcher(SimulatorAI simulatorAI) {
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
        ActionInfo actionInfo = actors.getOrDefault(eventName, null);
        BPMNActor actor = ActionFactory.buildBpmnActor(simulatorAI, actionInfo);
        if (actor == null) {
            log.error("{} can't execute {}", property.getSimulatorId(), eventName);
            return;
        }
        actor.postAction(eventMessage);
        if (!actor.isDone()) {
            simulatorAI.addTask(new BPMNTask(actionInfo.getActionName(), actor, eventMessage));
        } else {
            actor.afterAction(eventMessage);
            actor.reportDone(eventMessage);
        }

    }

    public void registerActionInfo(String actorName, ActionInfo actionInfo) {
        actors.put(actorName, actionInfo);
    }

}
