package org.mysim.quickstart.simulators.deliveryman;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.log.ActionLog;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.task.SimulationTask;

import java.util.Collection;

@Slf4j
public class DeliverymanAI extends ResourceAI {
    @Override
    public void step() {
        SimulatorProperty property = getSimulatorProperty();
        log.info("{}:step,current turn={}", property.getSimulatorId(), property.getTurn());
        super.step();
    }

    @Override
    public Collection<SimulationTask> planning(Collection<SimulationTask> tasks) {
        return super.planning(tasks);
    }

    @Override
    public void logAction(ActionLog actionLog) {
        super.logAction(actionLog);
    }
}
