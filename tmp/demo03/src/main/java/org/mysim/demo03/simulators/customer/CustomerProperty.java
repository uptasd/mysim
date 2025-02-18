package org.mysim.demo03.simulators.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.status.SimulatorProperty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProperty extends SimulatorProperty {
    int tmp;
}
