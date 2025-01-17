package org.mysim.core.rt.scheduler;

import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.Simulator;

import java.util.HashMap;


@Slf4j
public class ScheduledByProcessor implements AnnotationProcessingStrategy {
    SimulationContainer simulationContainer;


    public ScheduledByProcessor(SimulationContainer simulationContainer) {
        this.simulationContainer = simulationContainer;
    }

    @Override
    public void processAnnotation(Simulator simulator) {
        Class<?> clz = simulator.getClass();
        ScheduledBy scheduledBy = clz.getAnnotation(ScheduledBy.class);
        Class<? extends Scheduler> schedulerClass = scheduledBy.schedulerClass();
        String schedulerId = scheduledBy.schedulerID();
        int priority = scheduledBy.priority();
        Scheduler scheduler;
        if (!simulationContainer.containsScheduler(schedulerId)) {
            scheduler = SchedulerFactory.buildScheduler(schedulerId, priority, schedulerClass);
            simulationContainer.loadScheduler(scheduler);
        } else {
            scheduler = simulationContainer.getSchedulers().get(schedulerId);
        }
        scheduler.loadSimulator(simulator);
        simulator.getSimulatorProperty().setSchedulerId(schedulerId);

    }
}
