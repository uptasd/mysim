package org.mysim.core.simulator.memory;

import org.mysim.core.task.SimulationTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleMemory implements SimulatorMemory {
    private final ArrayList<SimulationTask> simulationTasks = new ArrayList<>();

    @Override
    public void addTask(SimulationTask task) {
        simulationTasks.add(task);
    }

    @Override
    public void clearFinishedTask() {
        simulationTasks.removeIf(SimulationTask::isDone);
    }


    @Override
    public Collection<SimulationTask> getTasks() {
        return simulationTasks;
    }

    @Override
    public Collection<SimulationTask> getHistory() {
        return List.of();
    }
}
