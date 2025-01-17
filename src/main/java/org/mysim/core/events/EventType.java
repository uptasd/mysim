package org.mysim.core.events;

public enum EventType {
    SYSTEM_EVENT("SYSTEM_EVENT"),
    BPMN_EVENT("BPMN_EVENT"),
    EXTERNAL_EVENT("EXTERNAL_EVENT")
    ;
    final String name;

    EventType(String name) {
        this.name = name;
    }
}
