package org.mysim.core.simulator;

import org.mysim.core.events.action.external.ExternalEventPayload;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SimMessageFactory;
import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.simulator.ai.EventSimulatorAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.EventSimulatorProperty;
import org.mysim.core.simulator.status.SimulatorProperty;


public abstract class EventSimulator extends Simulator {

    public EventSimulator(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        super(simulatorProperty, simulatorAI);

    }


}
