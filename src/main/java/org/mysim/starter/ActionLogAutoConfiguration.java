package org.mysim.starter;

import org.mysim.config.ActionLogConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ActionLogConfig.class)
public class ActionLogAutoConfiguration {
}
