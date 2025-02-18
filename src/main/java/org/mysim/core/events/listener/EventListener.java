package org.mysim.core.events.listener;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.SimulationActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;

@AllArgsConstructor
@Slf4j
public class EventListener extends Behaviour {
    public final Simulator simulator;
    public MessageTemplate messageTemplate;
    public SimulationActor actor;


    @Override
    public final void action() {
        SimulatorAgent agent = simulator.getAgent();
        ACLMessage msg = agent.receive(messageTemplate);
        if (msg != null) {
            String content = msg.getContent();
            SimMessage simMessage = JsonUtils.jsonToObject(content, SimMessage.class);
            SimulatorProperty property = simulator.getSimulatorProperty();
//            log.info("{}", msg);
            if (simMessage.getTurn() < property.getTurn()) {
                log.warn("{} received expiration message: {} ", property.getSimulatorId(), simMessage);
                return;
            }
            actor.action(simMessage);
        } else {
            block();
        }


    }

    @Override
    public final boolean done() {
        return false;
    }

}
