package org.mysim.core.simulator.memory;

import org.mysim.core.task.SimulationTask;

import java.util.Collection;

public interface SimulatorMemory {
    void addTask(SimulationTask task);
    void clearFinishedTask();
    Collection<SimulationTask> getTasks();

    Collection<SimulationTask> getHistory();

}
