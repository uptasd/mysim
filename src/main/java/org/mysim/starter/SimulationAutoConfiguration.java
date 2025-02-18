package org.mysim.starter;

import org.mysim.config.SimulationConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SimulationConfig.class)
public class SimulationAutoConfiguration {
}
