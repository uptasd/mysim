package org.mysim.core.simulator.status;

import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mysim.core.simulator.config.RuleFactory;
import org.mysim.core.utils.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatusManager {
    private static final Map<String, Object> config = RuleFactory.getConfig();
    private static final String EXPRESSION = "expression";
    private static final String STATUS = "status";

    @SuppressWarnings(value = "all")
    public static void checkAndUpdateStates(SimulatorProperty properties, SimulatorStatus status) {
        String simulatorType = properties.getSimulatorType();
        String simulatorId = properties.getSimulatorId();
        List<Map<String, Object>> stateRules = (List<Map<String, Object>>) config.getOrDefault(simulatorType, null);
        if (stateRules == null) {
            //todo log
            return;
        }
        Map<String, Object> propertyMap = JsonUtils.objToMap(properties);
        for (Map<String, Object> rule : stateRules) {
            List<Map<String, Object>> conditions = (List<Map<String, Object>>) rule.get("conditions");
            String expression = (String) rule.get(EXPRESSION);
            String statusName = (String) rule.get(STATUS);
            boolean conditionMatch;
            try {
                conditionMatch = MVEL.evalToBoolean(expression, propertyMap);
            } catch (Exception e) {
                //todo log
                e.printStackTrace();
                continue; // 跳过当前规则
            }
            if (conditionMatch) {
                if (!status.containsStatus(statusName)) {
                    log.info("{}添加新状态:{}", simulatorId, statusName);
                    status.addStatus(statusName);
                }
            } else {
                if (status.containsStatus(statusName)) {
                    log.info("{}删除状态:{}", simulatorId, statusName);
                    status.removeStatus(statusName);
                }
            }
        }
    }


}
