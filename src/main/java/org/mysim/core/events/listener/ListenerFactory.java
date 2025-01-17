package org.mysim.core.events.listener;

import jade.lang.acl.MessageTemplate;
import org.mysim.core.events.action.dispacher.BPMNEventDispatcher;
import org.mysim.core.events.action.dispacher.ExternalEventDispatcher;
import org.mysim.core.events.action.dispacher.SystemEventDispatcher;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.SimulatorAI;

public class ListenerFactory {


    public static EventListener buildSystemEventListener(Simulator simulator,SystemEventDispatcher dispatcher) {
        MessageTemplate mt = MessageTemplateFactory.buildSystemEventMT();
        SimulatorAI simulatorAI = simulator.getSimulatorAI();
//        SystemEventDispatcher dispatcher = new SystemEventDispatcher(simulatorAI);
        return new EventListener(simulator, mt, dispatcher);
    }

    public static EventListener buildBPMNEventListener(Simulator simulator, BPMNEventDispatcher actor) {
        MessageTemplate mt = MessageTemplateFactory.buildBPMNEventMT();
//        BPMNEventDispatcher actor = simulator.getSimulatorAI().getBPMNEventDispatchActor();
        return new EventListener(simulator, mt, actor);
    }
    public static EventListener buildExternalEventListener(Simulator simulator, ExternalEventDispatcher actor) {
        MessageTemplate mt = MessageTemplateFactory.buildExternalEventMT();
//        BPMNEventDispatcher actor = simulator.getSimulatorAI().getBPMNEventDispatchActor();
        return new EventListener(simulator, mt, actor);
    }


}
