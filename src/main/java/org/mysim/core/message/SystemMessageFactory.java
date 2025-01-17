package org.mysim.core.message;

import org.mysim.core.events.EventType;
import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.events.action.system.CreateProcessPayLoad;
import org.mysim.core.events.action.system.PullContextPayLoad;
import org.mysim.core.events.action.system.PublishContextPayLoad;
import org.mysim.core.events.action.system.SearchByTypePayLoad;
import org.mysim.core.utils.JsonUtils;

import java.util.*;

public class SystemMessageFactory {

    public static SimMessage buildStepMessage() {
        return buildMessage(SystemEvents.STEP.name(), "");
    }

    public static SimMessage buildSyncTurnMessage(long turn) {
        return buildMessage(SystemEvents.SYNC_TURN.name(), turn);
    }

    public static SimMessage buildPublishContextMessage(Map<String, Object> args) {
        PublishContextPayLoad publishContextPayLoad = new PublishContextPayLoad(args);
        return buildMessage(SystemEvents.PUBLISH_CONTEXT.name(), publishContextPayLoad);
    }

    public static SimMessage buildPullContextMessage(Set<String> contexts) {
        PullContextPayLoad pullContextPayLoad = new PullContextPayLoad();
        pullContextPayLoad.setArgs(contexts);
        return buildMessage(SystemEvents.PULL_CONTEXT.name(), pullContextPayLoad);
    }

    public static SimMessage buildCreateProcessMessage(String processId, Map<Object, Object> contexts) {
        CreateProcessPayLoad payLoad = new CreateProcessPayLoad(processId, contexts);
        return buildMessage(SystemEvents.CREATE_PROCESS.name(), payLoad);
    }

    public static SimMessage buildSearchSimulatorByTypeMessage(List<String> simulatorType) {
        SearchByTypePayLoad payLoad = new SearchByTypePayLoad(simulatorType,new HashMap<>());
        return buildMessage(SystemEvents.SEARCH_BY_TYPE.name(), payLoad);
    }
    public static SimMessage buildTaskCompleteMessage(BpmnTaskInfo bpmnTaskInfo) {
        return buildMessage(SystemEvents.TASK_COMPLETE.name(), bpmnTaskInfo);
    }

    private static SimMessage buildMessage(String eventName, Object payload) {
        SimMessage simMessage = new SimMessage();
        simMessage.setEventType(EventType.SYSTEM_EVENT.name());
        simMessage.setEventName(eventName);
        simMessage.setPayLoad(JsonUtils.objectToJson(payload));
        return simMessage;
    }
}
