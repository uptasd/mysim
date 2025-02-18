package org.mysim.core.simulator.ai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.simulator.BpmnProcessProxy;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;
import org.mysim.core.simulator.config.ActionInfo;
import org.mysim.core.task.SimulationTask;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceAI extends BaseAI {
    public ResourceAI() {
        super();
    }

    public ResourceAI(Simulator simulator) {
        super(simulator);
    }

    @Override
    public void step() {
        Collection<SimulationTask> tasks = planning(memory.getTasks());
        for (SimulationTask task : tasks) {
            System.out.println("执行：" + task);
            task.action();
        }
        memory.clearFinishedTask();
    }


    public Collection<SimulationTask> planning(Collection<SimulationTask> tasks) {
        return tasks.stream()
                .findFirst()
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    public void registerBpmnActorInfo(String actionName, ActionInfo actionInfo) {
        SimulatorAgent agent = simulator.getAgent();
        agent.registerBpmnActionInfo(actionName, actionInfo);

    }

    public void registerExternalActor(String actionName, ExternalEventActor actor) {
        SimulatorAgent agent = simulator.getAgent();
        agent.registerExternalEventActor(actionName, actor);
    }


}
