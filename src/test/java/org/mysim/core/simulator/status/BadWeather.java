package org.mysim.core.simulator.status;

import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.simulator.ai.SimulatorAI;

import java.util.Map;

public class BadWeather extends ExternalEventActor {
    public BadWeather(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }

    @Override
    public void onActivated(Map<String, Object> args) {
        TestProperty property = (TestProperty) getSimulatorProperty();
        int speed = property.getSpeed();
        speed -= 10;
        property.setSpeed(speed);
    }

    @Override
    public void onFinished(Map<String, Object> args) {
        TestProperty property = (TestProperty) getSimulatorProperty();
        int speed = property.getSpeed();
        speed += 10;
        property.setSpeed(speed);
    }
}
