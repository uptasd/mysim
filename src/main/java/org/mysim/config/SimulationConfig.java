package org.mysim.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Data
@Component
@ConfigurationProperties(prefix = "simulation.container")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimulationConfig {
    private volatile long turn = 0;
    private long timeRatio = 1;
    private long communicateTimeout = 1000;

    public enum TimeUnit {
        SECOND,
        MINUTE,
        HOUR,
        DAY,
    }

    private ChronoUnit timeUnit = ChronoUnit.MINUTES;
    private String startTime = "2024-01-01 08:00:00";
    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Getter
    private static SimulationConfig instance;

    @PostConstruct
    private void init() {
        instance = this;
    }
}
