package org.mysim.core.rt.scheduler;

import jade.core.Agent;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.Simulator;

import java.util.Collection;

public interface Scheduler {
    void loadSimulator(Simulator simulator);

    void removeSimulator(String simulatorId);

    Collection<Simulator> getSimulators();

    void schedule();


    void syncTime(long turn);

    String getSchedulerId();

    int getPriority();

    Agent getAgent();

    void setSimulationContainer(BaseContainer container);
}
