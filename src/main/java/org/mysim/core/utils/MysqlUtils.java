package org.mysim.core.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mysim.config.ApplicationContextHolder;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@Component
//service工厂
public class MysqlUtils {
    public static Map<String, IService> serviceMap=new HashMap<>();
    @Autowired
    public MysqlUtils(ApplicationContext context) {
        Map<String, IService> services = context.getBeansOfType(IService.class);
        services.forEach((name, service) -> serviceMap.put(name, service));
    }

    public static IService getService(String serviceName) {
        return serviceMap.getOrDefault(serviceName, null);
    }
//    @Autowired
//    private ApplicationContext applicationContext;
//    public static boolean insert(Object obj, String tableName) {
//        // 构造Service名称，如：DeliveryOrder -> deliveryOrderServiceImpl
//        String serviceName = toServiceName(tableName);
//
//        // 从Spring上下文获取Service
//        Object serviceBean = ApplicationContextHolder.getApplicationContext().getBean(serviceName);
//
//        if (serviceBean instanceof IService) {
//            // 强转为IService<Mybatis Plus提供的接口>
//            IService<Object> service = (IService<Object>) serviceBean;
//
//            // 调用save方法
//            return service.save(obj);
//        } else {
//            throw new IllegalArgumentException("Service not found or not an instance of IService: " + serviceName);
//        }
//    }
//    private static String toServiceName(String tableName) {
//        // 表名首字母小写，并拼接ServiceImpl
//        return Character.toLowerCase(tableName.charAt(0)) + tableName.substring(1) + "ServiceImpl";
//    }
}
