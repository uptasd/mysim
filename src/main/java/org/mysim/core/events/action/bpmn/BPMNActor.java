package org.mysim.core.events.action.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.action.ActionContext;
import org.mysim.core.events.action.BpmnActionConfiguration;
import org.mysim.core.events.action.SimulationActor;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.StatusManager;
import org.mysim.core.utils.JsonUtils;

import java.util.*;

@Slf4j
public abstract class BPMNActor extends SimulationActor {
    public BpmnActionConfiguration configuration;

    public BPMNActor(SimulatorAI simulatorAI, BpmnActionConfiguration configuration) {
        super(simulatorAI);
        this.configuration = configuration;
    }


    @Override
    public void action(SimMessage simMessage) {
        buildContext(simMessage);
        execute(context);
        StatusManager.checkAndUpdateStates(getSimulatorProperty(), getSimulatorStatus());
    }

    public void postAction(SimMessage simMessage) {
        buildContext(simMessage);
        pullContext();
        postExecute(context);
        StatusManager.checkAndUpdateStates(getSimulatorProperty(), getSimulatorStatus());
    }

    public void afterAction(SimMessage simMessage) {
        buildContext(simMessage);
        afterExecute(context);
        publishContext();
        StatusManager.checkAndUpdateStates(getSimulatorProperty(), getSimulatorStatus());
    }

    @Override
    public void buildContext(SimMessage simMessage) {
        super.buildContext(simMessage);
        BpmnTaskInfo taskInfo = JsonUtils.jsonToObject(simMessage.getPayLoad(), BpmnTaskInfo.class);
        context.setContext(ActionContext.BPMN_TASK_INFO, taskInfo);
    }

    public void postExecute(ActionContext context) {

    }

    public void afterExecute(ActionContext context) {

    }

    public boolean isDone() {
        return true;
    }


    private void pullContext() {
        if (configuration == null ||
                configuration.getSubscribedContext() == null ||
                configuration.getSubscribedContext().isEmpty()) {
            return;
        }
        BpmnTaskInfo taskInfo = getTaskInfo();
        if (taskInfo == null) {
            return;
        }

        List<String> subscribedContext = configuration.getSubscribedContext();
        String envId = String.valueOf(taskInfo.getProcessInstanceKey());
        if (subscribedContext.isEmpty()) {
            return;
        }
        Set<String> keys = new HashSet<>(subscribedContext);
        Map<String, Object> pulledContext = simulatorAI.pullContext(envId, keys);
        if (pulledContext != null && !pulledContext.isEmpty()) {
            context.setContext(pulledContext);
        }

    }

    private void publishContext() {
        if (configuration == null ||
                configuration.getPublishedContext() == null ||
                configuration.getPublishedContext().isEmpty()) {
            return;
        }

        BpmnTaskInfo taskInfo = getTaskInfo();
        List<String> publishedContext = configuration.getPublishedContext();

        if (taskInfo == null || publishedContext.isEmpty()) {
            return;
        }
        Map<String, Object> ctx = new HashMap<>();
        for (String key : publishedContext) {
            ctx.put(key, context.getContext(key));
        }
        String envId = String.valueOf(taskInfo.getProcessInstanceKey());
        simulatorAI.publishContext(envId, ctx);

    }

    public BpmnTaskInfo getTaskInfo() {
        SimMessage eventMessage = getSimMessage();
        if (eventMessage == null) {
            return null;
        }
        String payLoad = eventMessage.getPayLoad();
        return JsonUtils.jsonToObject(payLoad, BpmnTaskInfo.class);
    }


    public void reportDone(SimMessage simMessage) {
        BpmnTaskInfo bpmnTaskInfo = JsonUtils.jsonToObject(simMessage.getPayLoad(), BpmnTaskInfo.class);
        SimMessage taskDoneMessage = SystemMessageFactory.buildTaskCompleteMessage(bpmnTaskInfo);
        String bpmnProcessProxyId = simulatorAI.getContainer().getBpmnProxySimulatorId();
        simulatorAI.sendMessage(bpmnProcessProxyId, taskDoneMessage);
    }

}
