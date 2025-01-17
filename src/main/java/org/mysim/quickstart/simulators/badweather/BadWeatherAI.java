package org.mysim.quickstart.simulators.badweather;

import org.mysim.core.simulator.EventSimulator;
import org.mysim.core.simulator.ai.EventSimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.Map;

public class BadWeatherAI extends EventSimulatorAI {


    @Override
    public boolean activateEvent() {
        SimulatorProperty property = getSimulatorProperty();
        //测试，只在第一个周期产生事件
        return property.getTurn()==1;
    }

    @Override
    public Map<String, Object> buildStartEventArgs() {
        return Map.of();
    }

    @Override
    public Map<String, Object> buildFinishEventArgs() {
        return Map.of();
    }
}
