package org.mysim.quickstart.schedule2;

import org.junit.jupiter.api.Test;
import org.mysim.core.rt.container.BaseContainer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduleTest {
    //测试容器的调度
    @Test
    public void testStep() {
        BaseContainer container = new BaseContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        for (int i = 0; i < 5; i++) {
            container.step(1000);
        }
    }
    //测试Zeebe工作流引擎的业务调度
    //框架会自动读取并部署simulation/bpmn下的所有bpmn文件
    @Test
    public void testBpmnSchedule() {
        BaseContainer container = new BaseContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        container.loadSimulatorFromRes("simulation/init-properties/Customer.json");
        for (int i = 0; i < 10; i++) {
            container.step(1000);
        }
    }

}
