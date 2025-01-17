package org.mysim.core.rt.scheduler;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ScheduledBy {
    Class<? extends Scheduler> schedulerClass() default ConcurrentScheduler.class;
    String schedulerID() default "DefaultScheduler";
    int priority() default 0;

}
