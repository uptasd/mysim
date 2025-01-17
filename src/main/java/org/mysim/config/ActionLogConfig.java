package org.mysim.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "simulation.log.action")
public class ActionLogConfig {
    //默认不自动生成动作日志
    private boolean autoLog = false;
    //动作日志的输出地点
    private LogTarget target = LogTarget.CONSOLE;

    // 定义日志输出目标的枚举
    public enum LogTarget {
        MYSQL,
        CONSOLE,
        BOTH
    }
    @Getter
    private static ActionLogConfig instance;
    @PostConstruct
    private void init() {
        instance = this;
    }
}
