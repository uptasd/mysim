package org.mysim.core.simulator.ai;


import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.system.PullContext;
import org.mysim.core.events.action.system.PublishContext;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class EnvironmentSimulatorAI extends BaseAI {
    private final ConcurrentHashMap<String, Object> contexts = new ConcurrentHashMap<>();

    public EnvironmentSimulatorAI(Simulator simulator) {
        this.simulator = simulator;
        SimulatorAgent agent = simulator.getAgent();
        agent.registerSystemEventActor(SystemEvents.PULL_CONTEXT.name(), new PullContext(this));
        agent.registerSystemEventActor(SystemEvents.PUBLISH_CONTEXT.name(), new PublishContext(this));

    }

    public void setContext(String key, String value) {
        contexts.put(key, value);
    }

    public void setContext(Map<String, Object> map) {
        if (map == null) return;
        contexts.putAll(map);
    }

    public Object getContext(String key) {
        return contexts.getOrDefault(key, "");
    }

    public Map<String, Object> getContexts(Set<String> keys) {
        HashMap<String, Object> ret = new HashMap<>();
        for (String key : keys) {
            if (!contexts.containsKey(key)) {
                continue;
            }
            Object value = contexts.get(key);
            ret.put(key, value);
        }
        return ret;
    }

    @Override
    public void doDelete() {
        super.doDelete();
        contexts.clear();
    }
}
