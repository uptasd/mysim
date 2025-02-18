package org.mysim.starter;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mybatis.spring.annotation.MapperScan;
import org.mysim.core.log.ActionLog;
import org.mysim.core.utils.ActionLogUtils;
import org.mysim.core.utils.StatusUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ComponentScan(basePackages = {"org.mysim.core.log"})
@MapperScan(basePackages = {"org.mysim.core.log"})
public class UtilsAutoConfig {
    @Bean
    public ActionLogUtils actionLogUtils(IService<ActionLog> service) {
        return new ActionLogUtils(service);
    }
    @Bean
    public StatusUtils statusUtils(RedisTemplate<String, String> template){
        return new StatusUtils(template);
    }
}
