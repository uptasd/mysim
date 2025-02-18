package org.mysim.core.events.action.external;

import lombok.extern.slf4j.Slf4j;
import org.mysim.config.ActionLogConfig;
import org.mysim.config.SimulationConfig;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.log.ActionLog;
import org.mysim.core.events.action.SimulationActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.simulator.status.StatusManager;
import org.mysim.core.utils.JsonUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public abstract class ExternalEventActor extends SimulationActor {
    public boolean isActivated = false;

    public ExternalEventActor(SimulatorAI simulatorAI) {
        super(simulatorAI);
    }


    @Override
    public void action(SimMessage simMessage) {
        try {
            SimulatorProperty beforeProperty = (SimulatorProperty) JsonUtils.deepCopy(getSimulatorProperty());
            SimulatorStatus beforeStatus = (SimulatorStatus) JsonUtils.deepCopy(getSimulatorStatus());
            buildContext(simMessage);
            execute(context);
            StatusManager.checkAndUpdateStates(getSimulatorProperty(), getSimulatorStatus());
            String propertyUpdate = propertyUpdateDesc(beforeProperty, getSimulatorProperty());
            String statusUpdate = statusUpdateDesc(beforeStatus, getSimulatorStatus());
            if (ActionLogConfig.getInstance().isAutoLog()) {
                simulatorAI.logAction(buildActionLog(propertyUpdate, statusUpdate));
            }
            context.clearContext();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private ActionLog buildActionLog(String propertyUpdate, String statusUpdate) {
        SimulatorProperty property = getSimulatorProperty();
        ActionLog actionLog = new ActionLog();
        actionLog.setActorId(property.getSimulatorId());
        actionLog.setActionName(actionName);
        actionLog.setTurn(property.getTurn());
        actionLog.setPropertyDesc(propertyUpdate);
        actionLog.setStatusDesc(statusUpdate);
        return actionLog;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage simMessage = getSimMessage();
        SimulatorProperty property = getSimulatorProperty();
        String payLoad = simMessage.getPayLoad();
        ExternalEventPayload externalEventPayload = JsonUtils.jsonToObject(payLoad, ExternalEventPayload.class);
        handleEvent(property, simMessage, externalEventPayload);
    }

    private void handleEvent(SimulatorProperty property, SimMessage simMessage, ExternalEventPayload externalEventPayload) {
        String simulatorId = property.getSimulatorId();
        String eventName = simMessage.getEventName();
        int eventFlag = externalEventPayload.getEventFlag();
        switch (eventFlag) {
            case 0:
                handleEventStart(simulatorId, eventName, externalEventPayload);
                break;
            case 1:
                handleEventEnd(simulatorId, eventName, externalEventPayload);
                break;
//            case 2:
//                handleEventStartThenFinished(simulatorId, eventName, externalEventPayload);
//                break;
            default:
                log.error("unknown external event flag:{},in msg:{}", eventFlag, simMessage);

        }
    }

    private void handleEventStart(String simulatorId, String eventName, ExternalEventPayload eventContext) {
        if (isActivated) {
            log.info("{}已经感知到事件{}正在发生，忽略此事件", simulatorId, eventName);
        } else {
            log.info("{} 感知到事件发生:{}", simulatorId, eventName);
            isActivated = true;
//            Map<String, Object> propertyMap = getPropertyMap();
            onActivated(eventContext.getArgs());
//            MapToProperty(propertyMap);
        }
    }

    private void handleEventEnd(String simulatorId, String eventName, ExternalEventPayload eventContext) {
        if (!isActivated) {
            log.info("{}已经感知到事件{}已经结束,忽略此事件", simulatorId, eventName);
        } else {
            log.info("{} 感知到事件结束:{}", simulatorId, eventName);
            isActivated = false;
//            Map<String, Object> propertyMap = getPropertyMap();
            onFinished(eventContext.getArgs());
//            MapToProperty(propertyMap);
        }
    }

    private void handleEventStartThenFinished(String simulatorId, String eventName, ExternalEventPayload eventContext) {
        if (isActivated) {
            log.debug("{}已经感知到事件{}正在发生，忽略此事件", simulatorId, eventName);
        } else {
//            Map<String, Object> propertyMap = getPropertyMap();
            onActivated(eventContext.getArgs());
            onFinished(eventContext.getArgs());
//            MapToProperty(propertyMap);
        }
    }

    private Map<String, Object> getPropertyMap() {
        SimulatorProperty property = getSimulatorProperty();
        return JsonUtils.objToMap(property);
    }

    private void MapToProperty(Map<String, Object> propertyMap) {
        SimulatorProperty newProperty = JsonUtils.mapToObj(propertyMap, getSimulatorProperty().getClass());
        Simulator simulator = simulatorAI.getSimulator();
        simulator.setSimulatorProperty(newProperty);
    }

    public abstract void onActivated(Map<String, Object> args);

    public abstract void onFinished(Map<String, Object> args);


}
