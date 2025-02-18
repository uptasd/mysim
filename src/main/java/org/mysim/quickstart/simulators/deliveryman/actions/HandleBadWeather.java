package org.mysim.quickstart.simulators.deliveryman.actions;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.quickstart.simulators.deliveryman.DeliverymanProperty;

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
