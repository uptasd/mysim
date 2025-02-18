package org.mysim.core.rt.container;

import jade.wrapper.StaleProxyException;
import org.junit.jupiter.api.Test;
import org.mysim.core.simulator.Simulator;

import java.util.List;

class BaseContainerTest {

    @Test
    void loadSimulatorFromRes() throws StaleProxyException {
        BaseContainer container = new BaseContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        List<Simulator> allSimulators = container.getAllSimulators();
        System.out.println(allSimulators);
    }
}