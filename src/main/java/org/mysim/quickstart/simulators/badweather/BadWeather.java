package org.mysim.quickstart.simulators.badweather;

import org.mysim.core.simulator.EventSimulator;
import org.mysim.core.simulator.ai.EventSimulatorAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.EventSimulatorProperty;
import org.mysim.core.simulator.status.SimulatorProperty;


public class BadWeather extends EventSimulator {


    public BadWeather(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        super(simulatorProperty, simulatorAI);
    }
}
