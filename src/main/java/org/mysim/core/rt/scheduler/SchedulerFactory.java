package org.mysim.core.rt.scheduler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SchedulerFactory {
    //同时像所有仿真体发送step报文
    public static ConcurrentScheduler buildConCurrentScheduler(String simulatorId, int priority) {
        SchedulerProperty property = new SchedulerProperty();
        property.setSimulatorId(simulatorId);
        property.setPriority(priority);
        return new ConcurrentScheduler(property);
    }

    public static Scheduler buildScheduler(String simulatorId, int priority, Class<? extends Scheduler> schedulerClass) {
        SchedulerProperty property = new SchedulerProperty();
        property.setSimulatorId(simulatorId);
        property.setPriority(priority);
        try {
            Constructor<? extends Scheduler> declaredConstructor = schedulerClass.getDeclaredConstructor(SchedulerProperty.class);
            return declaredConstructor.newInstance(property);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
