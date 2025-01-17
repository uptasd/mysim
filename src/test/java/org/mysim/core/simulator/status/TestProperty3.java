package org.mysim.core.simulator.status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestProperty3 extends SimulatorProperty{
    private int field1;
    private int field2;
    private int field3;
}
