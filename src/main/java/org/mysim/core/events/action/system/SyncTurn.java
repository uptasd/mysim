package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncTurn extends SystemActor {
    private static final Logger log = LoggerFactory.getLogger(SyncTurn.class);

    public SyncTurn(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void execute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        SimMessage eventMessage = getSimMessage();
        long turn = Long.parseLong(eventMessage.getPayLoad());
        property.setTurn(turn);
        log.debug("{}已同步周期为{}", property.getSimulatorId(), property.getTurn());
        SimMessage reply = eventMessage.buildReply(simulatorAI,"");
        simulatorAI.sendMessage(eventMessage.getSenderId(),reply);
    }
}
