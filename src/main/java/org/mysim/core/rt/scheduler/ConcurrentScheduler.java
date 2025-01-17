package org.mysim.core.rt.scheduler;

import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.message.SimMessage;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.Simulator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentScheduler extends Simulator implements Scheduler {
    private final Map<String, Simulator> simulators = new ConcurrentHashMap<>();
    private final SchedulerProperty property;

    public ConcurrentScheduler(SchedulerProperty property) {
        super(property);
        this.property = property;
    }

    @Override
    public void loadSimulator(Simulator simulator) {
        simulators.put(simulator.getSimulatorProperty().getSimulatorId(), simulator);
        simulator.getSimulatorProperty().setSchedulerId(property.getSimulatorId());
    }

    @Override
    public Collection<Simulator> getSimulators() {
        return simulators.values();
    }

    @Override
    public void schedule() {
        Collection<Simulator> simulators = getSimulators();
        SimMessage stepMessage = SystemMessageFactory.buildStepMessage();
        List<String> targetIds = simulators.stream().map(simulator -> simulator.getSimulatorProperty().getSimulatorId())
                .toList();
        boardCastMessage(targetIds, stepMessage);
    }

    @Override
    public void syncTime(long turn) {
        getSimulatorProperty().setTurn(turn);
        Collection<Simulator> simulators = getSimulators();
        SimMessage stepMessage = SystemMessageFactory.buildSyncTurnMessage(turn);
        List<String> targetIds = simulators.stream().map(simulator -> simulator.getSimulatorProperty().getSimulatorId())
                .toList();
        String conversationId = boardCastMessage(targetIds, stepMessage);
        blockingReceive(conversationId, 2 * 1000, targetIds.size());
    }

    public void removeSimulator(String simulatorId) {
        simulators.remove(simulatorId);
    }

    @Override
    public String getSchedulerId() {
        return property.getSimulatorId();
    }

    @Override
    public int getPriority() {
        return property.getPriority();
    }

    @Override
    public void setSimulationContainer(SimulationContainer container) {
        setContainer(container);
    }

    @Override
    public void deregister() {

    }
}
