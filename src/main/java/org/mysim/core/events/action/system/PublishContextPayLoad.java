package org.mysim.core.events.action.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishContextPayLoad {
    private Map<String, Object> args;
}
