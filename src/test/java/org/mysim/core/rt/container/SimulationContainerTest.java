package org.mysim.core.rt.container;

import jade.wrapper.StaleProxyException;
import org.junit.jupiter.api.Test;
import org.mysim.core.simulator.Simulator;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimulationContainerTest {

    @Test
    void loadSimulatorFromRes() throws StaleProxyException {
        SimulationContainer container = new SimulationContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        List<Simulator> allSimulators = container.getAllSimulators();
        System.out.println(allSimulators);
    }
}