package org.mysim.demo03.simulators.badweather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.status.EventSimulatorProperty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadWeatherProperty extends EventSimulatorProperty {
    private int level;
}
