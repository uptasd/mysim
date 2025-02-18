package org.mysim.core.rt.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.Simulator;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AnnotationProcessor {
    private static final Map<Class<? extends Annotation>, AnnotationProcessingStrategy> strategies = new HashMap<>();

    public AnnotationProcessor(BaseContainer baseContainer) {
        addStrategy(ScheduledBy.class, new ScheduledByProcessor(baseContainer));
    }

    public static void addStrategy(Class<? extends Annotation> annotationClass, AnnotationProcessingStrategy strategy) {
        strategies.put(annotationClass, strategy);
    }
    public void process(Simulator simulator) {
        Class<? extends Simulator> clazz = simulator.getClass();
        for (Annotation annotation : clazz.getAnnotations()){
            AnnotationProcessingStrategy strategy = strategies.get(annotation.annotationType());
            if(strategy!=null){
                log.debug("对{} 执行策略:{}",simulator.getSimulatorProperty().getSimulatorId(),strategy.getClass().getSimpleName());
                strategy.processAnnotation(simulator);
            }
        }

    }
}
