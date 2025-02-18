package org.mysim.demo03.simulators.restaurant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.demo03.simulators.common.property.Location;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantProperty extends SimulatorProperty {
    Location location;
    Integer stock;
}
