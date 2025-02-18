package org.mysim.core.simulator;

import lombok.Getter;
import lombok.Setter;
import org.mysim.core.message.MessageProxy;
import org.mysim.core.message.SimMessage;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.rt.scheduler.Scheduler;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.simulator.status.StatusManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@Data
@Setter
@Getter
public abstract class Simulator implements SimulatorAction {
    volatile SimulatorProperty simulatorProperty;
    SimulatorStatus simulatorStatus;
    SimulatorAI simulatorAI;
    SimulatorAgent agent;
    MessageProxy messageProxy;
    BaseContainer container;

    public Simulator(String simulatorId) {
        simulatorProperty = new SimulatorProperty();
        simulatorProperty.setSimulatorId(simulatorId);
        setSimulatorAI(new ResourceAI(this));
        init();
    }

    public Simulator(String simulatorId, SimulatorAI simulatorAI) {
        simulatorProperty = new SimulatorProperty();
        simulatorProperty.setSimulatorId(simulatorId);
        this.simulatorAI = simulatorAI;
        init();
    }

    public Simulator(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        this.simulatorProperty = simulatorProperty;
        this.simulatorAI = simulatorAI;
        init();
    }

    public Simulator(SimulatorProperty simulatorProperty) {
        this.simulatorProperty = simulatorProperty;
        this.simulatorAI = new ResourceAI(this);
        init();
    }

    private void init() {
        agent = new SimulatorAgent(this);
        simulatorStatus = new SimulatorStatus();
        messageProxy = new MessageProxy(agent);
        simulatorAI.setSimulator(this);
        StatusManager.checkAndUpdateStates(simulatorProperty, simulatorStatus);
    }

    @Override
    public String sendMessage(String targetId, SimMessage simMessage) {
        return messageProxy.send(targetId, simMessage);
    }

    @Override
    public String boardCastMessage(Collection<String> targetIds, SimMessage simMessage) {
        return messageProxy.boardCast(targetIds, simMessage);
    }

    @Override
    public SimMessage blockingReceive(String conversationId, long timeMills) {
        return messageProxy.blockingReceive(conversationId, timeMills);
    }

    @Override
    public List<SimMessage> blockingReceive(String conversationId, long timeMills, Set<String> expectedIds) {
        return messageProxy.blockingReceive(conversationId, timeMills, expectedIds);
    }

    @Override
    public List<SimMessage> blockingReceive(String conversationId, long timeMills, int num) {
        return messageProxy.blockingReceive(conversationId, timeMills, num);
    }

    @Override
    public void publishMessage() {
        //todo
    }

    @Override
    public void deregister() {
        simulatorAI.doDelete();
        String schedulerId = simulatorProperty.getSchedulerId();
        Scheduler scheduler = container.getSchedulerById(schedulerId);
        scheduler.removeSimulator(getSimulatorProperty().getSimulatorId());
    }

    public Map<String, List<String>> getAllActorId() {
        return agent.getAllActorId();
    }
}
