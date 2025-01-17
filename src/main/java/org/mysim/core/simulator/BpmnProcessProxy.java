package org.mysim.core.simulator;

import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.system.CreateProcess;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.ai.BpmnProcessProxyAI;
import org.mysim.core.simulator.status.SimulatorProperty;

@Slf4j
public class BpmnProcessProxy extends Simulator {
//    SimulationContainer container;

    public BpmnProcessProxy(String bpmnProcessProxyId) {
        super(bpmnProcessProxyId);
        simulatorAI = new BpmnProcessProxyAI(this);
        simulatorAI.setSimulator(this);
    }
}
