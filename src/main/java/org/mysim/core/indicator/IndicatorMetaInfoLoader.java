package org.mysim.core.indicator;

import lombok.Getter;
import org.mysim.config.SimulationConfigConst;
import org.mysim.core.simulator.config.SimulatorMetaInfo;
import org.mysim.core.simulator.config.SimulatorMetaInfoLoader;
import org.mysim.core.utils.JsonUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicatorMetaInfoLoader {
    @Getter
    static Map<String, Map<String, IndicatorMetaInfo>> indicatorMetaInfoMap = new HashMap<>();

    static {
        loadConfig(SimulationConfigConst.SERVICE_CONFIG);
    }

    public static void loadConfig(String configFilePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = IndicatorMetaInfoLoader.class.getClassLoader().getResourceAsStream(configFilePath)) {
            List<Object> infos = yaml.loadAs(inputStream, List.class);
            for (Object obj : infos) {
                ServiceMetaInfo info = JsonUtils.mapToObj(obj, ServiceMetaInfo.class);
                List<IndicatorMetaInfo> indicatorMetaInfos = info.getIndicators();
                if (indicatorMetaInfos == null) continue;
                String serviceName = info.getServiceName();
                for (IndicatorMetaInfo indicatorMetaInfo : indicatorMetaInfos) {
                    String indicatorName = indicatorMetaInfo.getName();
                    Map<String, IndicatorMetaInfo> map = indicatorMetaInfoMap.getOrDefault(serviceName, new HashMap<>());
                    map.put(indicatorName, indicatorMetaInfo);
                    indicatorMetaInfoMap.put(serviceName, map);
                }
            }
        } catch (IOException e) {
            //todo log
            return;
        }
    }

}
