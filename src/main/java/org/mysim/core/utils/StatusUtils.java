package org.mysim.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.mysim.core.rt.container.MyContainer.PROPERTY_KEY_PREFIX;

@Component
public class StatusUtils {
    static RedisTemplate<String, String> template;
    static ObjectMapper mapper;
    static Map<String, Set<String>> simulatorIdMap = new HashMap<>();

    @Autowired
    public StatusUtils(RedisTemplate<String, String> template) {
        StatusUtils.template = template;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void setTemplate(RedisTemplate<String, String> template) {
        StatusUtils.template = template;
    }

    public static void setProperty(SimulatorProperty property) {
        String simulatorId = property.getSimulatorId();
        String simulatorType = property.getSimulatorType();
        Set<String> simulatorIds = simulatorIdMap.getOrDefault(simulatorType, new HashSet<>());
        simulatorIds.add(simulatorId);
        simulatorIdMap.put(simulatorType, simulatorIds);
        set(PROPERTY_KEY_PREFIX + simulatorId, property);
    }

    public static String getPropertyBySimulatorId(String simulatorId) {
       return get(PROPERTY_KEY_PREFIX + simulatorId);
    }

    public static void set(String key, Object object) {
        try {
            String s = mapper.writeValueAsString(object);
            template.opsForValue().set(key, s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return template.opsForValue().get(key);
    }

    public static List<String> getSimulatorIdsByType(String simulatorType) {
        List<String> ans = new ArrayList<>();
        if (!simulatorIdMap.containsKey(simulatorType)) return ans;
        Set<String> ids = simulatorIdMap.get(simulatorType);
        ans.addAll(ids);
        return ans;
    }

    public static Map<String, Integer> getTypeStatics() {
        Map<String, Integer> ans = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : simulatorIdMap.entrySet()) {
            String key = entry.getKey();
            Set<String> value = entry.getValue();
            ans.put(key, value.size());
        }
        return ans;
    }
}
