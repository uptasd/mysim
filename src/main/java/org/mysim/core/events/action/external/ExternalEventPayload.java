package org.mysim.core.events.action.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalEventPayload {
    private int eventFlag;// 0:started,1:finished
    private Map<String, Object> args;
}
