package org.mysim.core.simulator;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.simulator.ai.BpmnProcessProxyAI;

@Slf4j
public class BpmnProcessProxy extends Simulator {
//    SimulationContainer container;

    public BpmnProcessProxy(String bpmnProcessProxyId) {
        super(bpmnProcessProxyId);
        simulatorAI = new BpmnProcessProxyAI(this);
        simulatorAI.setSimulator(this);
    }
}
