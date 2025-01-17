package org.mysim.core.simulator.ai;

import org.mysim.core.log.ActionLog;
import org.mysim.core.events.action.ServiceLog;
import org.mysim.core.message.SimMessage;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.task.SimulationTask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SimulatorAI {
    void addTask(SimulationTask simulationTask);

    void step();

    Simulator getSimulator();

    void setSimulator(Simulator simulator);

    SimulatorProperty getSimulatorProperty();

    SimulatorStatus getSimulatorStatus();

    String sendMessage(String targetId, SimMessage simMessage);

    String boardCastMessage(Collection<String> targetIds, SimMessage message);

    SimMessage blockingReceive(String conversationId, long timeMills);

    void publishContext(String envId, Map<String, Object> context);

    Map<String, Object> pullContext(String envId, Set<String> keys);

    void publishMessage(String envId, SimMessage message);

    void createBpmnProcess(String processId, Map<Object, Object> initContext);

    void logAction(ActionLog actionLog);

    void logService(ServiceLog serviceLog);

    List<String> searchSimulatorByType(String simulatorType);

    Map<String, List<String>> searchSimulatorByType(List<String> simulatorTypes);

    void doDelete();
    SimulationContainer getContainer();

}
