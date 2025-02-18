package org.mysim.core.indicator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMetaInfo {
    String serviceName;
    List<IndicatorMetaInfo> indicators;
}
