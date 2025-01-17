package org.mysim.core.simulator.status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestProperty2 extends SimulatorProperty {
    private int health;
}
