package org.mysim.core.rt.scheduler;

import jade.core.Agent;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.Simulator;

import java.util.Collection;

public interface Scheduler {
    void loadSimulator(Simulator simulator);

    Collection<Simulator> getSimulators();

    void schedule();


    void syncTime(long turn);

    String getSchedulerId();

    int getPriority();

    Agent getAgent();
    void setSimulationContainer(SimulationContainer container);
}
