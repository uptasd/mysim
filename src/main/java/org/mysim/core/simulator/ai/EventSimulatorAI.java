package org.mysim.core.simulator.ai;

import org.mysim.core.events.action.external.ExternalEventPayload;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SimMessageFactory;
import org.mysim.core.simulator.EventSimulator;
import org.mysim.core.simulator.SearchSimulator;
import org.mysim.core.simulator.status.EventSimulatorProperty;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class EventSimulatorAI extends BaseAI {
    long lastActivatedTurn = 0;
    boolean isActivated = false;

    @Override
    public void step() {
        EventSimulatorProperty property = (EventSimulatorProperty) getSimulatorProperty();
        int continueTurn = property.getEventContinueTurn();
        long currentTurn = property.getTurn();
        if (isActivated) {
            if (lastActivatedTurn + continueTurn <= currentTurn) {
                boardCastEventEnd();
                isActivated = false;
            }
        } else {
            if (activateEvent()) {
                boardCastEventStart();
                isActivated = true;
                lastActivatedTurn = currentTurn;
            }
        }
    }

    private void boardCastEventStart() {
        EventSimulatorProperty property = (EventSimulatorProperty) getSimulatorProperty();
        ExternalEventPayload externalEventPayload = new ExternalEventPayload(0, buildStartEventArgs());
        sendEventMsg(property, externalEventPayload);
    }

    private void boardCastEventEnd() {
        EventSimulatorProperty property = (EventSimulatorProperty) getSimulatorProperty();
        ExternalEventPayload externalEventPayload = new ExternalEventPayload(1, buildFinishEventArgs());
        sendEventMsg(property, externalEventPayload);
    }

    private void boardCastEventStartThenEnd() {
        EventSimulatorProperty property = (EventSimulatorProperty) getSimulatorProperty();
        ExternalEventPayload externalEventPayload = new ExternalEventPayload(2, buildStartEventArgs());
        sendEventMsg(property, externalEventPayload);
    }

    private void sendEventMsg(EventSimulatorProperty property, ExternalEventPayload externalEventPayload) {
        SimMessage simMessage = SimMessageFactory.buildExternalEventMessage(getSimulatorProperty().getSimulatorType(), externalEventPayload);
        Map<String, List<String>> subscribers = searchSimulatorByType(property.getSubscriberTypes());
        if (subscribers == null) {
            return;
        }
        List<String> targets = new ArrayList<>();
        for (List<String> simulators : subscribers.values()) {
            targets.addAll(simulators);
        }
        boardCastMessage(targets, simMessage);
    }

    public abstract boolean activateEvent();


    public abstract Map<String, Object> buildStartEventArgs();

    public abstract Map<String, Object> buildFinishEventArgs();
}
