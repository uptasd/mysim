package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.EnvironmentSimulatorAI;
import org.mysim.core.utils.JsonUtils;

import java.util.Map;

public class PullContext extends SystemActor {
    EnvironmentSimulatorAI environmentSimulatorAI;

    public PullContext(EnvironmentSimulatorAI simulatorAI) {
        super(simulatorAI);
        environmentSimulatorAI = simulatorAI;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage eventMessage = getSimMessage();
        String payLoad = eventMessage.getPayLoad();
        PullContextPayLoad pullContextPayLoad = JsonUtils.jsonToObject(payLoad, PullContextPayLoad.class);
        Map<String, Object> contexts = environmentSimulatorAI.getContexts(pullContextPayLoad.args);
        pullContextPayLoad.setRet(contexts);
        String senderId = eventMessage.getSenderId();
        String replyPayLoad = JsonUtils.objectToJson(pullContextPayLoad);
        SimMessage simMessage = eventMessage.buildReply(environmentSimulatorAI, replyPayLoad);
        environmentSimulatorAI.sendMessage(senderId, simMessage);
    }
}
