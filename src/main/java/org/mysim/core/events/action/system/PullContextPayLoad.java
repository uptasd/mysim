package org.mysim.core.events.action.system;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class PullContextPayLoad {
    Set<String> args;
    Map<String, Object> ret;
}
