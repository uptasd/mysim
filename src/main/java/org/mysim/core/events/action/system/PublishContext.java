package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.EnvironmentSimulatorAI;
import org.mysim.core.utils.JsonUtils;

import java.util.Map;

public class PublishContext extends SystemActor {
    EnvironmentSimulatorAI environmentSimulatorAI;

    public PublishContext(EnvironmentSimulatorAI simulatorAI) {
        super(simulatorAI);
        environmentSimulatorAI = simulatorAI;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage eventMessage = getSimMessage();
        String payLoad = eventMessage.getPayLoad();
        PublishContextPayLoad publishContextPayLoad = JsonUtils.jsonToObject(payLoad, PublishContextPayLoad.class);
        Map<String, Object> ctx = publishContextPayLoad.getArgs();
        environmentSimulatorAI.setContext(ctx);
    }
}
