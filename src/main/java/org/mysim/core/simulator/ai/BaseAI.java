package org.mysim.core.simulator.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mysim.config.ActionLogConfig;
import org.mysim.core.log.ActionLog;
import org.mysim.core.events.action.ServiceLog;
import org.mysim.core.events.action.system.PullContextPayLoad;
import org.mysim.core.events.action.system.SearchByTypePayLoad;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.memory.SimpleMemory;
import org.mysim.core.simulator.memory.SimulatorMemory;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.task.SimulationTask;
import org.mysim.core.utils.JsonUtils;
import org.mysim.core.utils.ActionLogUtils;

import java.util.*;

@Getter
@Setter
@Slf4j
public abstract class BaseAI implements SimulatorAI {
    public Simulator simulator;
    public SimulatorMemory memory;
    public static final long PULL_CONTEXT_TIMEOUT = 1000;
    public static final long SEARCH_SIMULATOR_TIMEOUT = 1000;

    public BaseAI(Simulator simulator) {
        this();
        this.simulator = simulator;
    }

    public BaseAI() {
        memory = new SimpleMemory();
    }


    public void addTask(SimulationTask simulationTask) {
        memory.addTask(simulationTask);
    }

    @Override
    public void step() {

    }

    @Override
    public void logAction(ActionLog actionLog) {
        ActionLogConfig config = ActionLogConfig.getInstance();
        switch (config.getTarget()) {
            case MYSQL:
                ActionLogUtils.save(actionLog);
                break;
            case CONSOLE:
                log.info("{}输出动作日志：{}", getSimulatorProperty().getSimulatorId(), actionLog);
                break;
            default:
                break;
        }
    }

    @Override
    public void logService(ServiceLog serviceLog) {
        //todo
    }

    @Override
    public SimulatorProperty getSimulatorProperty() {
        return simulator.getSimulatorProperty();
    }

    @Override
    public SimulatorStatus getSimulatorStatus() {
        return simulator.getSimulatorStatus();
    }

    @Override
    public String sendMessage(String targetId, SimMessage simMessage) {
        return simulator.sendMessage(targetId, simMessage);
    }

    @Override
    public String boardCastMessage(Collection<String> targetIds, SimMessage message) {
        return simulator.boardCastMessage(targetIds, message);
    }

    @Override
    public SimMessage blockingReceive(String conversationId, long timeMills) {
        return simulator.blockingReceive(conversationId, timeMills);
    }

    @Override
    public Map<String, Object> pullContext(String envId, Set<String> keys) {
        SimMessage simMessage = SystemMessageFactory.buildPullContextMessage(keys);
        String sessionId = sendMessage(envId, simMessage);
        SimMessage reply = blockingReceive(sessionId, PULL_CONTEXT_TIMEOUT);
        PullContextPayLoad pullContextPayLoad = JsonUtils.jsonToObject(reply.getPayLoad(), PullContextPayLoad.class);
        return pullContextPayLoad.getRet();
    }

    @Override
    public void publishContext(String envId, Map<String, Object> context) {
        SimMessage simMessage = SystemMessageFactory.buildPublishContextMessage(context);
        sendMessage(envId, simMessage);
    }

    //与publishContext类似
    @Override
    public void publishMessage(String envId, SimMessage message) {
        //todo
    }

    @Override
    public List<String> searchSimulatorByType(String simulatorType) {
        SimMessage simMessage = SystemMessageFactory.buildSearchSimulatorByTypeMessage(Collections.singletonList(simulatorType));
//        SimulatorProperty property = getSimulatorProperty();
        //todo check null
        String simulatorSearcherId = getContainer().getSimulatorSearcherId();
        String sessionId = sendMessage(simulatorSearcherId, simMessage);
        SimMessage ret = blockingReceive(sessionId, SEARCH_SIMULATOR_TIMEOUT);
        SearchByTypePayLoad payLoad = JsonUtils.jsonToObject(ret.getPayLoad(), new TypeReference<>() {
        });
        Map<String, List<String>> simulators = payLoad.getSimulators();
        if (simulators == null) return null;
        return simulators.getOrDefault(simulatorType, null);
    }

    @Override
    public Map<String, List<String>> searchSimulatorByType(List<String> simulatorTypes) {
        SimMessage simMessage = SystemMessageFactory.buildSearchSimulatorByTypeMessage(simulatorTypes);
        SimulatorProperty property = getSimulatorProperty();
        //todo check null
        String simulatorSearcherId = getContainer().getSimulatorSearcherId();
        String sessionId = sendMessage(simulatorSearcherId, simMessage);
        SimMessage ret = blockingReceive(sessionId, SEARCH_SIMULATOR_TIMEOUT);
        SearchByTypePayLoad payLoad = JsonUtils.jsonToObject(ret.getPayLoad(), new TypeReference<>() {
        });
        return payLoad.getSimulators();
    }

    @Override
    public void doDelete() {
        simulator.getAgent().doDelete();
    }

    public void createBpmnProcess(String processId, Map<Object, Object> contexts) {
        SimMessage simMessage = SystemMessageFactory.buildCreateProcessMessage(processId, contexts);
//        SimulatorProperty property = getSimulatorProperty();
        String bpmnProcessProxyId = getContainer().getBpmnProxySimulatorId();
        sendMessage(bpmnProcessProxyId, simMessage);
    }

    @Override
    public SimulationContainer getContainer() {
        return simulator.getContainer();
    }
}
