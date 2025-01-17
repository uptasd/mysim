package org.mysim.core.simulator.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionInfo {
    private String actionName;
    private String actionClass;
    private List<String> subscribedContext;
    private List<String> publishedContext;
}
