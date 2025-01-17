package org.mysim.quickstart.simulators.restaurant.actions;

import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorStatus;

import java.util.Map;

public class HandleBadWeather extends ExternalEventActor {
    public HandleBadWeather(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void onActivated( Map<String, Object> args) {

    }

    @Override
    public void onFinished( Map<String, Object> args) {

    }
}
