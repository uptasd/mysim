package org.mysim.core.events.action.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProcessPayLoad {
    private String processId;
    private Map<Object, Object> contexts;
}
