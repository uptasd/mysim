package org.mysim.core.simulator.status;

import org.mysim.core.events.action.BpmnActionConfiguration;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;

public class Attack extends BPMNActor {
    public Attack(SimulatorAI simulatorAI, BpmnActionConfiguration configuration) {
        super(simulatorAI, configuration);
    }

    @Override
    public void postExecute(ActionContext context) {

    }

    @Override
    public void afterExecute(ActionContext context) {

    }


    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage eventMessage = getSimMessage();
        TestProperty2 property = (TestProperty2) getSimulatorProperty();
        int cur = property.getHealth();
        property.setHealth(cur - 5);
//                        System.out.println("be attacked,health update:" + cur + "->" + property.getHealth());
        SimMessage reply = eventMessage.buildReply(simulatorAI, "");
        simulatorAI.sendMessage(eventMessage.getSenderId(), reply);
    }

}
