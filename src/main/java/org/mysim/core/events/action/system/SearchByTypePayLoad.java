package org.mysim.core.events.action.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByTypePayLoad {
    private List<String> simulatorTypes;
    private Map<String,List<String>> simulators;
}
