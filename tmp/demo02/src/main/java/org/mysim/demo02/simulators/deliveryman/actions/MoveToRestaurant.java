package org.mysim.demo02.simulators.deliveryman.actions;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.BpmnActionConfiguration;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.Map;

@Slf4j
public class MoveToRestaurant extends BPMNActor {
    int cnt = 2;

    public MoveToRestaurant(SimulatorAI simulatorAI, BpmnActionConfiguration configuration) {
        super(simulatorAI, configuration);
    }

    @Override
    public void postExecute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        BpmnTaskInfo taskInfo = getTaskInfo();
        log.info("{} received task:{}", property.getSimulatorId(), taskInfo);
    }

    @Override
    public void execute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        BpmnTaskInfo taskInfo = getTaskInfo();
        cnt--;
        log.info("{} is moving to restaurant,taskInfo:{}", property.getSimulatorId(), taskInfo);
    }

    @Override
    public void afterExecute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        BpmnTaskInfo taskInfo = getTaskInfo();
        log.info("{} finished task:{}", property.getSimulatorId(), taskInfo);
        //存放流程的上下文
        Map<String, Object> testKey = Map.of("testKey", getSimulatorProperty().getSimulatorId());
        log.info("{} put context:{}", property.getSimulatorId(), testKey);
        putContext(testKey);
    }

    @Override
    public boolean isDone() {
        return cnt <= 0;
    }


}
