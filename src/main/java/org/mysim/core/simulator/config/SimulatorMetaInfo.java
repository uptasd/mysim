package org.mysim.core.simulator.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulatorMetaInfo {
    private String simulatorType;
    private String simulatorClass;
    private String simulatorAIClass;
    private String propertyClass;
    private List<ActionInfo> actions;
}
