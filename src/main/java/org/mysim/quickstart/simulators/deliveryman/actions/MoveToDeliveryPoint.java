package org.mysim.quickstart.simulators.deliveryman.actions;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.BpmnActionConfiguration;
import org.mysim.core.events.action.bpmn.BPMNActor;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;

@Slf4j
public class MoveToDeliveryPoint extends BPMNActor {
    int cnt = 2;

    public MoveToDeliveryPoint(SimulatorAI simulatorAI, BpmnActionConfiguration configuration) {
        super(simulatorAI, configuration);
    }

    @Override
    public void postExecute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        SimMessage eventMessage = getSimMessage();
        String payLoad = eventMessage.getPayLoad();
        BpmnTaskInfo eventPayLoad = JsonUtils.jsonToObject(payLoad, BpmnTaskInfo.class);
        log.info("{} received task:MoveToDeliveryPoint，taskId:{}", property.getSimulatorId(), eventPayLoad.getJobKey());
        Object testKey = getContext("testKey");
        //获取流程的上下文
        log.info("{} get context:{}", property.getSimulatorId(), testKey);

    }

    @Override
    public void afterExecute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        log.info("{} finished task:MoveToDeliveryPoint", property.getSimulatorId());

    }

    @Override
    public boolean isDone() {
        if(cnt <= 0){
            System.out.println("move to delivery point done");
        }
        return cnt <= 0;
    }

    @Override
    public void execute(ActionContext context) {
        SimulatorProperty property = getSimulatorProperty();
        BpmnTaskInfo taskInfo = getTaskInfo();
        cnt--;
        log.info("{} is moving to delivery point,taskInfo:{}", property.getSimulatorId(), taskInfo);
    }

}
