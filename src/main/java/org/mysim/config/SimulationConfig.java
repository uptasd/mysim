package org.mysim.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Data
@Component
@ConfigurationProperties(prefix = "simulation.container")
public class SimulationConfig {
    private long turn = 0;
    private long timeRatio = 1;

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
