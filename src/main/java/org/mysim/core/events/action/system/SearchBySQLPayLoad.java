package org.mysim.core.events.action.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBySQLPayLoad {
    private String condition;
    private List<String> simulators;
}
