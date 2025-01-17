package org.mysim.core.task;

import org.mysim.core.events.action.bpmn.BPMNActor;

/*
 * use to wrap the simulation action,if task done report to sender
 */
public interface SimulationTask {
    void action();

    boolean isDone();

    void loadTask(BPMNActor BPMNActor);

    BPMNActor getActor();

    void reportTaskDone();
}
