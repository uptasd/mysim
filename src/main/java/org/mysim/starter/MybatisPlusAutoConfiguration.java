package org.mysim.starter;

import org.mysim.config.MybatisPlusConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MybatisPlusConfig.class)
public class MybatisPlusAutoConfiguration {
}
