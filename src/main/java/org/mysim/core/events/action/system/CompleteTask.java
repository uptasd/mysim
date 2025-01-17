package org.mysim.core.events.action.system;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.BpmnProcessProxyAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.utils.CamundaUtils;

import java.util.HashMap;

@Slf4j
public class CompleteTask extends SystemActor {
    BpmnProcessProxyAI bpmnProcessProxyAI;

    public CompleteTask(BpmnProcessProxyAI bpmnProcessProxyAI) {
        super(bpmnProcessProxyAI);
        this.bpmnProcessProxyAI = bpmnProcessProxyAI;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage simMessage = getSimMessage();
        String reporter = simMessage.getSenderId();
        BpmnTaskInfo payLoad = getPayLoad(BpmnTaskInfo.class);
        CamundaUtils.completeTask(payLoad.getJobKey(), new HashMap<>());
        log.debug("{} report task done ,taskName={},taskId={},processId={}", reporter, payLoad.getTaskName(), payLoad.getJobKey(), payLoad.getProcessInstanceKey());
    }
}
