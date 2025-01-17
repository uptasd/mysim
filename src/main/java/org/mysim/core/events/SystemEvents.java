package org.mysim.core.events;

public enum SystemEvents {
    STEP("SYSTEM-EVENT-STEP"),
    SYNC_TURN("SYNC-TURN"),
    PULL_CONTEXT("PULL_CONTEXT"),
    PUBLISH_CONTEXT("PUBLISH_CONTEXT"),
    CREATE_PROCESS("CREATE_PROCESS"),
    TASK_COMPLETE("TASK_COMPLETE"),
    SEARCH_BY_TYPE("SEARCH_BY_TYPE"),
    ;
    final String name;

    SystemEvents(String name) {
        this.name = name;
    }
}
