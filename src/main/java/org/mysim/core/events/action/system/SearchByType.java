package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.message.SimMessage;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchByType extends SystemActor {
    public SearchByType(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage eventMessage = getSimMessage();
        SearchByTypePayLoad payLoad = getPayLoad(SearchByTypePayLoad.class);
        Map<String, List<String>> simulators = getSimulators(payLoad);
        payLoad.setSimulators(simulators);
        SimMessage replyMsg = eventMessage.buildReply(simulatorAI, JsonUtils.objectToJson(payLoad));
        simulatorAI.sendMessage(eventMessage.getSenderId(), replyMsg);
    }

    private Map<String, List<String>> getSimulators(SearchByTypePayLoad payLoad) {
        List<String> simulatorTypes = payLoad.getSimulatorTypes();
        SimulationContainer container = simulatorAI.getContainer();
        Map<String, List<String>> simulators = new HashMap<>();
        if (simulatorTypes != null && !simulatorTypes.isEmpty()) {
            for (String type : simulatorTypes) {
                List<String> simulatorIds = container.getSimulatorIdByType(type);
                simulators.put(type, simulatorIds);
            }
        }
        return simulators;
    }
}
