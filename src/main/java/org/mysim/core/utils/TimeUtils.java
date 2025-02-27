package org.mysim.core.utils;

import org.mysim.config.SimulationConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class TimeUtils {
    static SimulationConfig config = SimulationConfig.getInstance();

    public static LocalDateTime getLogicTime(long turn) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(config.getDateTimeFormat());
        LocalDateTime startTime = LocalDateTime
                .parse(config.getStartTime(), formatter);
        long ratio = config.getTimeRatio();
        ChronoUnit timeUnit = config.getTimeUnit();
        return startTime.plus(turn * ratio, timeUnit);
    }

    public static long turnDuration2Seconds(long turnDuration) {
        long timeRatio = config.getTimeRatio();
        long seconds = config.getTimeUnit().getDuration().getSeconds();
        return turnDuration * timeRatio * seconds;
    }
}
