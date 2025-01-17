package org.mysim.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatusUtils {
    static RedisTemplate<String, String> template;
    static ObjectMapper mapper;

//    @Autowired
//    public RedisUtils(RedisTemplate<String, String> template) {
//        RedisUtils.template = template;
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//    }

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
}
