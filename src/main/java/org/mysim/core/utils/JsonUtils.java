package org.mysim.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mysim.core.simulator.config.SimulatorFactory;
import org.mysim.core.exception.JsonDeserializationException;
import org.mysim.core.exception.JsonSerializationException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void registerModule(Module module) {
        mapper.registerModule(module);
    }

    public static Object deepCopy(Object object) throws IOException, ClassNotFoundException {
        String s = JsonUtils.objectToJson(object);
        Map<String, Object> map = JsonUtils.jsonToMap(s);
        return JsonUtils.mapToObj(map, object.getClass());
    }

    public synchronized static String objectToJson(Object obj) {
        String ret;
        try {
            ret = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            String errorMessage = String.format("Failed to serialize object:%s to JSON", obj);
            throw new JsonSerializationException(errorMessage, e);
        }
        return ret;
    }

    public static <T> T resToObj(String resourcePath, TypeReference<T> typeReference) {
        try (InputStream resourceAsStream = SimulatorFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            return mapper.readValue(resourceAsStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(String jsonData, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonData, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(String jsonData, Class<T> objType) {
        if (jsonData == null || jsonData.isEmpty()) {
            throw new IllegalArgumentException("Input JSON data cannot be null or empty");
        }
        try {
            return mapper.readValue(jsonData, objType);
        } catch (IOException e) {
            // 记录错误信息，抛出自定义异常
            String errorMessage = String.format("Failed to deserialize JSON: '%s' to type: '%s'", jsonData, objType.getName());
            throw new JsonDeserializationException(errorMessage, e);
        }
    }

    public static Map<String, Object> jsonToMap(String jsonData) {
        if (jsonData == null) return new HashMap<>();
        try {
            return mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T mapToObj(Object map, Class<T> clazz) {
        return mapper.convertValue(map, clazz);
    }

    public static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> resultMap = new HashMap<>();
        Class<?> clazz = obj.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 允许访问私有字段
                try {
                    resultMap.put(field.getName(), field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
        return resultMap;
    }

}
