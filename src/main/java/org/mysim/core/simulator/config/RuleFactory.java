package org.mysim.core.simulator.config;

import lombok.Getter;
import org.mysim.config.SimulationConfigConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class RuleFactory {
    @Getter
    private static Map<String, Object> config;
    private static final Logger log = LoggerFactory.getLogger(RuleFactory.class);

    static {
        loadConfig(SimulationConfigConst.STATE_RULES);
    }

    public static void loadConfig(String resFileNamePath) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(resFileNamePath);
        Yaml yaml = new Yaml();
        config = yaml.load(inputStream);
    }
}
