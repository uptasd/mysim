package org.mysim.quickstart.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import org.junit.jupiter.api.Test;
import org.mysim.core.log.ActionLog;
import org.mysim.core.utils.ActionLogUtils;
import org.mysim.core.utils.MysqlUtils;
import org.mysim.quickstart.pojo.DeliveryOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class TestMysql {
    @Test
    void testInsertActionLog() {
        ActionLog log = new ActionLog();
        log.setActionName("demo");
        log.setTurn(0L);
        log.setActorId("actor1");
        ActionLogUtils.save(log);
    }

    @Test
    void testGetActionLog() {
        List<ActionLog> actor1 = ActionLogUtils.getAllActionLogBySimulatorId("actor1");
        System.out.println(actor1);
    }

    @Test
    void testCustomLog() {
        DeliveryOrder order = new DeliveryOrder();
        order.setGeneratedTime(LocalDateTime.now());
        IService deliveryOrderService = MysqlUtils.getService("deliveryOrderServiceImpl");
        deliveryOrderService.save(order);
    }

}
