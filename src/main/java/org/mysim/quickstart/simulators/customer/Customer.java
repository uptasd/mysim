package org.mysim.quickstart.simulators.customer;

import org.mysim.core.rt.scheduler.ScheduledBy;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

@ScheduledBy(priority = 1,schedulerID = "customerScheduler")
public class Customer extends ResourceSimulator {
    public Customer(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
        super(simulatorProperty, simulatorAI);
    }
}
