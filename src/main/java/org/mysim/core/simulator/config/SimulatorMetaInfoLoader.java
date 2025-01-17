package org.mysim.core.simulator.config;

import lombok.Getter;
import org.mysim.config.SimulationConfigConst;
import org.mysim.core.utils.JsonUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimulatorMetaInfoLoader {
    @Getter
    static List<SimulatorMetaInfo> metaInfo = new ArrayList<>();

    static {
        loadConfig(SimulationConfigConst.SIMULATOR_CONFIG);
    }

    public static void loadConfig(String configFilePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = SimulatorFactory.class.getClassLoader().getResourceAsStream(configFilePath)) {
            List<Object> infos = yaml.loadAs(inputStream, List.class);
            for (Object obj : infos) {
                SimulatorMetaInfo info = JsonUtils.mapToObj(obj, SimulatorMetaInfo.class);
                if (info.getSimulatorType() == null || info.getSimulatorType().isEmpty()) {
                    continue;
                }
                metaInfo.add(info);
            }
        } catch (IOException e) {
            //todo log
            return;
        }
    }

}
