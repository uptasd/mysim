package org.mysim.core.rt.container;

import org.mysim.core.simulator.Simulator;

import java.util.List;

public interface SimulationContainer {
    void step(long blockingMillTimes);
    void step();
    boolean loadSimulator(Simulator simulator);

    void loadSimulatorFromRes(String resFileName);

    void loadSimulatorFromDir(String restDir);
    List<Simulator> getAllSimulators();
}
