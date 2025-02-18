package org.mysim.core.indicator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorMetaInfo {
    String name;
    String implement;
    String className;
    String formula;
}
