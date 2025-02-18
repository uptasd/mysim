package org.mysim.demo03.simulators.deliveryman.actions;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.demo03.simulators.deliveryman.DeliverymanProperty;

import java.util.Map;

@Slf4j
public class HandleBadWeather extends ExternalEventActor {
    public HandleBadWeather(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }


    @Override
    public void onActivated( Map<String, Object> args) {
        log.info("bad weather started");
        DeliverymanProperty deliverymanProperty = (DeliverymanProperty) getSimulatorProperty();
        Double speed = deliverymanProperty.getSpeed();
        deliverymanProperty.setSpeed(speed - 5);
    }

    @Override
    public void onFinished( Map<String, Object> args) {
        log.info("bad weather finished");
        DeliverymanProperty deliverymanProperty = (DeliverymanProperty) getSimulatorProperty();
        Double speed = deliverymanProperty.getSpeed();
        deliverymanProperty.setSpeed(speed + 5);
    }
}
