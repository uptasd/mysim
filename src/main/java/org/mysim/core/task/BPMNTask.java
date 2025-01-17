package org.mysim.core.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.message.SimMessage;

@Data
@AllArgsConstructor
public class BPMNTask implements SimulationTask {
    String actionName;
    BPMNActor actor;
    SimMessage simMessage;

    @Override
    public void action() {
        actor.action(simMessage);
        if (isDone()) {
            actor.afterAction(simMessage);
            reportTaskDone();
        }
    }

    @Override
    public boolean isDone() {
        return actor.isDone();
    }


    @Override
    public void loadTask(BPMNActor BPMNActor) {
        actor = BPMNActor;
    }

    @Override
    public BPMNActor getActor() {
        return actor;
    }


    @Override
    public void reportTaskDone() {
        actor.reportDone(simMessage);
    }
}
