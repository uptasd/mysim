package org.mysim.core.events.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionContext {

    public static final String SIMULATOR_PROPERTY = "SIMULATOR_PROPERTY";
    public static final String SIMULATOR_STATUS = "SIMULATOR_STATUS";
    public static final String MESSAGE_CONTEXT = "MESSAGE_CONTEXT";
    public static final String BPMN_TASK_INFO = "BPMN_TASK_INFO";
    HashMap<String, Object> contexts;

    public ActionContext() {
        contexts = new HashMap<>();
    }

    public Object getContext(String key) {
        return contexts.getOrDefault(key, null);
    }

    public Map<String, Object> getContext(List<String> keys) {
        Map<String, Object> ret = new HashMap<>();
        for (String key : keys) {
            ret.put(key,contexts.getOrDefault(key, null));
        }
        return ret;
    }

    public void setContext(String key, Object context) {
        contexts.put(key, context);
    }

    public void setContext(Map<String, Object> ctx) {
        contexts.putAll(ctx);
    }

    public void clearContext() {
        contexts.clear();
    }
}
