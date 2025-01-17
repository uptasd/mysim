package org.mysim.core.events.action.bpmn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BpmnTaskInfo {
    long jobKey;
    long processInstanceKey;
    String taskName;
}
