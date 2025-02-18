package org.mysim.quickstart.indicator4;

import org.junit.jupiter.api.Test;
import org.mysim.core.indicator.IndicatorFactory;
import org.mysim.quickstart.pojo.DeliveryOrder;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.mysim.quickstart.utils.ServiceFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;

@SpringBootTest
public class IndicatorCalculateTest {
    @Test
    void addDelivery_order() {
        DeliveryOrderService deliveryService = (DeliveryOrderService) ServiceFactory.getService("deliveryOrderServiceImpl");
        for (int i = 0; i < 5; i++) {
            DeliveryOrder order = new DeliveryOrder();
            order.setStatus("done");
            deliveryService.save(order);
        }
        for (int i = 0; i < 5; i++) {
            DeliveryOrder order = new DeliveryOrder();
            order.setStatus("processing");
            deliveryService.save(order);
        }
    }

    @Test
    public void testSuccessNum() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object successNum = IndicatorFactory.getIndicator("delivery", "successNum");
        System.out.println("successNum:"+successNum);
        Object totalNum = IndicatorFactory.getIndicator("delivery", "totalNum");
        System.out.println("totalNum:"+totalNum);
        Object successRate = IndicatorFactory.getIndicator("delivery", "successRate");
        System.out.println("successRate:"+successRate);


    }
}
