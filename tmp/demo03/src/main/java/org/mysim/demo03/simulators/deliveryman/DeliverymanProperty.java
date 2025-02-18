package org.mysim.demo03.simulators.deliveryman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.demo03.simulators.common.property.Location;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverymanProperty extends SimulatorProperty {
    Location location;
    Double speed;
    LocalTime workHour;
    LocalTime offHour;
}
