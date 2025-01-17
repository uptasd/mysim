package org.mysim.core.simulator.config;

import org.mysim.core.events.action.*;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionFactory {
    static HashMap<String, List<ActionInfo>> actionMaps = new HashMap<>();

    private enum ActorType {
        TASK, REACTION, UNKNOWN
    }

    static {
        List<SimulatorMetaInfo> metaInfos = SimulatorMetaInfoLoader.getMetaInfo();
        for (SimulatorMetaInfo metaInfo : metaInfos) {
            String simulatorType = metaInfo.getSimulatorType();
            actionMaps.put(simulatorType, metaInfo.getActions());
        }
    }

    public static ExternalEventActor buildExternalEventActor(SimulatorAI ai, ActionInfo actionInfo) {
        if (actionInfo == null) return null;
        try {
            Class<? extends ExternalEventActor> actionClass = (Class<? extends ExternalEventActor>) Class.forName(actionInfo.getActionClass());
            Constructor<? extends ExternalEventActor> constructor = actionClass.getDeclaredConstructor(SimulatorAI.class);
            ExternalEventActor externalEventActor = constructor.newInstance(ai);
            externalEventActor.setActionName(actionInfo.getActionName());
            return externalEventActor;
        } catch (ReflectiveOperationException e) {
            //todo log error
            e.printStackTrace();
            return null;
        }
    }

    public static BPMNActor buildBpmnActor(SimulatorAI ai, ActionInfo actionInfo) {
        if (actionInfo == null) return null;
        try {
            String actionName = actionInfo.getActionName();
            Class<? extends BPMNActor> actionClass = (Class<? extends BPMNActor>) Class.forName(actionInfo.getActionClass());
            BpmnActionConfiguration bpmnActionConfiguration = new BpmnActionConfiguration(actionName, actionInfo.getSubscribedContext(), actionInfo.getPublishedContext());
            Constructor<? extends BPMNActor> constructor = actionClass.getDeclaredConstructor(SimulatorAI.class, BpmnActionConfiguration.class);
            BPMNActor bpmnActor = constructor.newInstance(ai, bpmnActionConfiguration);
            bpmnActor.setActionName(actionName);
            return bpmnActor;
        } catch (ReflectiveOperationException e) {
            //todo log error
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, ActionInfo> getBPMNActorInfos(Simulator simulator) {
        Map<String, ActionInfo> ret = new HashMap<>();
        List<ActionInfo> actionInfos = getALlActionInfos(simulator);
        if (actionInfos == null) return ret;
        for (ActionInfo actionInfo : actionInfos) {
            ActorType type = getType(actionInfo);
            if (type != ActorType.TASK) {
                continue;
            }
            String actionName = actionInfo.getActionName();
            ret.put(actionName, actionInfo);
        }
        return ret;
    }

    public static Map<String, ExternalEventActor> getExternalActors(Simulator simulator) {
        Map<String, ExternalEventActor> ret = new HashMap<>();
        List<ActionInfo> actionInfos = getALlActionInfos(simulator);
        if (actionInfos == null) return ret;
        for (ActionInfo actionInfo : actionInfos) {
            ActorType type = getType(actionInfo);
            if (type != ActorType.REACTION) {
                continue;
            }
            ExternalEventActor actor = buildExternalEventActor(simulator.getSimulatorAI(), actionInfo);
            ret.put(actionInfo.getActionName(), actor);
        }
        return ret;
    }

    private static ActorType getType(ActionInfo actionInfo) {
        String actionClassName = actionInfo.getActionClass();
        if (actionClassName == null) {
            return ActorType.UNKNOWN;
        }
        Class<?> actionClass = getClassByName(actionClassName);
        if (actionClass == null) {
            return ActorType.UNKNOWN; // 类加载失败时，直接返回 UNKNOWN
        }

        if (isAssignableFrom(actionClass, BPMNActor.class)) {
            return ActorType.TASK;
        }

        if (isAssignableFrom(actionClass, ExternalEventActor.class)) {
            return ActorType.REACTION;
        }

        return ActorType.UNKNOWN;
    }

    private static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // 可以选择记录日志或监控
            return null;
        }
    }

    private static boolean isAssignableFrom(Class<?> targetClass, Class<?> baseClass) {
        return baseClass.isAssignableFrom(targetClass);
    }

    private static List<ActionInfo> getALlActionInfos(Simulator simulator) {
        List<ActionInfo> ret = new ArrayList<>();
        SimulatorProperty property = simulator.getSimulatorProperty();
        if (property == null) {
            return ret;
        }
        String simulatorType = property.getSimulatorType();
        if (!actionMaps.containsKey(simulatorType)) {
            return ret;
        }
        return actionMaps.get(simulatorType);
    }

}
