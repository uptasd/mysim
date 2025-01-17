package org.mysim.quickstart.simulators.badweather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.status.EventSimulatorProperty;
import org.mysim.core.simulator.status.SimulatorProperty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadWeatherProperty extends EventSimulatorProperty {
    private int level;
}
