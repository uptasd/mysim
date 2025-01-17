package org.mysim.core.simulator.status;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestProperty extends SimulatorProperty {
    private int health;
    private int mana;
    private int speed;

}
