package org.mysim.core.simulator.ai;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.mysim.config.CamundaConfig;
import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.bpmn.BpmnTaskInfo;
import org.mysim.core.events.action.system.CompleteTask;
import org.mysim.core.events.action.system.CreateProcess;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SimMessageFactory;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.EnvironmentSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.CamundaUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BpmnProcessProxyAI extends BaseAI {
    public static final String TASK_HANDLER = "handler";
    private final ConcurrentHashMap<Long, ActivatedJob> jobs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, EnvironmentSimulator> envs = new ConcurrentHashMap<>();

    public BpmnProcessProxyAI(Simulator simulator) {
        openListener();
        SimulatorAgent agent = simulator.getAgent();
        agent.registerSystemEventActor(SystemEvents.CREATE_PROCESS.name(), new CreateProcess(this));
        agent.registerSystemEventActor(SystemEvents.TASK_COMPLETE.name(), new CompleteTask(this));
    }

    public void createInstance(String processId, Map<Object, Object> contexts) {
        //todo
        if (processId == null) {
            return;
        }
        long processInstanceKey = CamundaUtils.createNewInstance(processId, contexts);
        loadEnvironment(processInstanceKey);
    }

    private void openListener() {
        ZeebeClient client = CamundaConfig.getZeebeClient();
        JobWorker jobWorker = client.newWorker()
                .jobType("io.camunda.zeebe:userTask")
                .handler(this::dispatch)
                .open();
    }

    private void openCleaner() {
        //todo

    }

    private void dispatch(JobClient client, ActivatedJob job) {
        Map<String, Object> input = job.getVariablesAsMap();
        String taskHandler = (String) input.getOrDefault(TASK_HANDLER, null);
        long key = job.getKey();
        if (jobs.containsKey(key)) {
            log.debug("process:{},已被分配", key);
            return;
        }
        if (taskHandler == null) {
            client.newThrowErrorCommand(job)
                    .errorCode("300")
                    .errorMessage("没有定义任务执行者")
                    .send()
                    .join();
            log.error("无法分配task：{}\n原因：handler为空", job);
            return;
        }
        log.debug("job dispatcher 已将任务:{},分配给：{},流程ID为:{},任务ID为:{}", job.getElementId(), taskHandler, job.getProcessInstanceKey(), job.getKey());
        String eventName = job.getElementId();
        BpmnTaskInfo payLoad = new BpmnTaskInfo();
        payLoad.setJobKey(key);
        payLoad.setProcessInstanceKey(job.getProcessInstanceKey());
        payLoad.setTaskName(job.getElementId());
        jobs.put(key, job);
        SimMessage simMessage = SimMessageFactory.buildBpmnEventMessage(eventName, payLoad);
//        log.debug("send msg:{}", simMessage);
        sendMessage(taskHandler, simMessage);
    }

    private void loadEnvironment(Long processInstanceKey) {
        if (envs.containsKey(processInstanceKey)) {
            return;
        }
        EnvironmentSimulator env = new EnvironmentSimulator(String.valueOf(processInstanceKey));
        envs.put(processInstanceKey, env);
//        SimulatorProperty property = getSimulatorProperty();
        SimulationContainer container = getContainer();
        container.loadSimulator(env);
    }
}
