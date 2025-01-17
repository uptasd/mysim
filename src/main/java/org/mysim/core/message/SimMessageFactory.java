package org.mysim.core.message;

import org.mysim.core.events.EventType;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.events.action.external.ExternalEventPayload;
import org.mysim.core.utils.JsonUtils;

public class SimMessageFactory {
    //用于触发仿真体的任务动作执行器
    public static SimMessage buildBpmnEventMessage(String eventName, BpmnTaskInfo eventPayLoad) {
        String jsonData = JsonUtils.objectToJson(eventPayLoad);
        return buildMessage(EventType.BPMN_EVENT.name(), eventName, jsonData);
    }

    //用于触发仿真体的系统动作执行器,具体的事件报文的生成见SystemMessageFactory
    @Deprecated
    public static SimMessage buildSystemMessage(String eventName, String payload) {
        return buildMessage(EventType.SYSTEM_EVENT.name(), eventName, payload);
    }

    //用于触发仿真体的外部事件动作执行器
    public static SimMessage buildExternalEventMessage(String eventName, ExternalEventPayload context) {
        String payload = JsonUtils.objectToJson(context);
        return buildMessage(EventType.EXTERNAL_EVENT.name(), eventName, payload);
    }

    private static SimMessage buildMessage(String eventType, String eventName, String payload) {
        return SimMessage.builder()
                .eventType(eventType)
                .eventName(eventName)
                .payLoad(payload)
                .build();
    }
}
