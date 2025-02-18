package org.mysim.core.simulator.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulatorProperty {
    private String simulatorId;
    private String simulatorType;
    private volatile long turn;
    private String schedulerId;
    private String envId;
//    private SimulationContainer container;

    public SimulatorProperty(String simulatorId) {
        this.simulatorId = simulatorId;
    }
}
