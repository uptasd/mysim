package org.mysim.core.simulator;

import jade.core.Agent;
import lombok.Getter;
import org.mysim.core.events.action.dispacher.SystemEventDispatcher;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.events.action.dispacher.BPMNEventDispatcher;
import org.mysim.core.events.action.dispacher.ExternalEventDispatcher;
import org.mysim.core.events.action.system.SystemActor;
import org.mysim.core.events.listener.ListenerFactory;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.config.ActionFactory;
import org.mysim.core.simulator.config.ActionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Getter
public class SimulatorAgent extends Agent {
    Simulator simulator;
    private final SystemEventDispatcher systemEventDispatcher;
    private final BPMNEventDispatcher bpmnEventDispatcher;
    private final ExternalEventDispatcher externalEventDispatcher;


    public SimulatorAgent(Simulator simulator) {
        SimulatorAI simulatorAI = simulator.getSimulatorAI();
        systemEventDispatcher = new SystemEventDispatcher(simulatorAI);
        bpmnEventDispatcher = new BPMNEventDispatcher(simulatorAI);
        externalEventDispatcher = new ExternalEventDispatcher(simulatorAI);
        this.simulator = simulator;
        initBPMNEventDispatcher();
        initExternalEventDispatcher();

    }

    private void initBPMNEventDispatcher() {
        Map<String, ActionInfo> actors = ActionFactory.getBPMNActorInfos(simulator);
        for (Map.Entry<String, ActionInfo> mp : actors.entrySet()) {
            String actionName = mp.getKey();
            ActionInfo actionInfo = mp.getValue();
            bpmnEventDispatcher.registerActionInfo(actionName, actionInfo);
        }
    }

    private void initExternalEventDispatcher() {
        Map<String, ExternalEventActor> actors = ActionFactory.getExternalActors(simulator);
        for (Map.Entry<String, ExternalEventActor> mp : actors.entrySet()) {
            String actionName = mp.getKey();
            ExternalEventActor actor = mp.getValue();
            externalEventDispatcher.registerExternalEventActor(actionName, actor);
        }
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(ListenerFactory.buildSystemEventListener(simulator, systemEventDispatcher));
        addBehaviour(ListenerFactory.buildBPMNEventListener(simulator, bpmnEventDispatcher));
        addBehaviour(ListenerFactory.buildExternalEventListener(simulator, externalEventDispatcher));
    }

    public void registerBpmnActionInfo(String actionName, ActionInfo actionInfo) {
        bpmnEventDispatcher.registerActionInfo(actionName, actionInfo);
    }

    public void registerExternalEventActor(String actionName, ExternalEventActor actor) {
        externalEventDispatcher.registerExternalEventActor(actionName, actor);
    }

    public void registerSystemEventActor(String actionName, SystemActor systemActor) {
        systemEventDispatcher.registerSystemEvent(actionName, systemActor);
    }

    public Map<String, List<String>> getAllActorId() {
        Map<String, List<String>> ret = new HashMap<>();
        List<String> bpmnActors = bpmnEventDispatcher.getAllActors();
        ret.put("BpmnActors", bpmnActors);
        List<String> externalActors = externalEventDispatcher.getAllActors();
        ret.put("ExternalActors", externalActors);
        List<String> systemActors = systemEventDispatcher.getAllActors();
        ret.put("SystemActors", systemActors);
        return ret;
    }
}
