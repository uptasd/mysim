package org.mysim.quickstart.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceFactory {
    public static Map<String, IService> serviceMap=new HashMap<>();
    public static DeliveryOrderService deliveryOrderService;

    @Autowired
    public ServiceFactory(ApplicationContext context) {
        Map<String, IService> services = context.getBeansOfType(IService.class);
        services.forEach((name, service) -> serviceMap.put(name, service));
    }

    public static IService getService(String serviceName) {
        return serviceMap.getOrDefault(serviceName, null);
    }
}
