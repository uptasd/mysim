package org.mysim.core.rt.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.Simulator;


@Slf4j
public class ScheduledByProcessor implements AnnotationProcessingStrategy {
    BaseContainer baseContainer;


    public ScheduledByProcessor(BaseContainer baseContainer) {
        this.baseContainer = baseContainer;
    }

    @Override
    public void processAnnotation(Simulator simulator) {
        Class<?> clz = simulator.getClass();
        ScheduledBy scheduledBy = clz.getAnnotation(ScheduledBy.class);
        Class<? extends Scheduler> schedulerClass = scheduledBy.schedulerClass();
        String schedulerId = scheduledBy.schedulerID();
        int priority = scheduledBy.priority();
        Scheduler scheduler;
        if (!baseContainer.containsScheduler(schedulerId)) {
            scheduler = SchedulerFactory.buildScheduler(schedulerId, priority, schedulerClass);
            baseContainer.loadScheduler(scheduler);
        } else {
            scheduler = baseContainer.getSchedulers().get(schedulerId);
        }
        scheduler.loadSimulator(simulator);
        simulator.getSimulatorProperty().setSchedulerId(schedulerId);

    }
}
