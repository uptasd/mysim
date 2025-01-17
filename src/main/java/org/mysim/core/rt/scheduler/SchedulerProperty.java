package org.mysim.core.rt.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.status.SimulatorProperty;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerProperty extends SimulatorProperty {
    private int priority;
}
