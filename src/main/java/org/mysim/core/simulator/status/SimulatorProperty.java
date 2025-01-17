package org.mysim.core.simulator.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mysim.core.rt.container.SimulationContainer;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulatorProperty {
    private String simulatorId;
    private String simulatorType;
    private long turn;
    private String schedulerId;
    private String envId;
//    private SimulationContainer container;

    public SimulatorProperty(String simulatorId) {
        this.simulatorId = simulatorId;
    }
}
