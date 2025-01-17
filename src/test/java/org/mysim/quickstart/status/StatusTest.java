package org.mysim.quickstart.status;

import org.junit.jupiter.api.Test;
import org.mysim.core.rt.container.SimulationContainer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StatusTest {
    // 外部事件测试+状态转移测试
    // 恶劣天气生成器会生成事件并发送到所有外卖骑手，骑手会响应这个事件，减少自身的速度属性
    // 最后根据速度值改变自身状态（高速、低速、正常）
    // 属性配置文件：simulation/config/state_rules.yaml
    // 事件响应动作类：org.mysim.quickstart.simulators.deliveryman.actions.HandleBadWeather
    // (这些动作类在simulator-config.yaml中配置)
    // 注意：在application.yml配置action-log.auto-log为true后会自动生成动作日志，可以在表action_log查看
    @Test
    public void testExternalEvent() {
        SimulationContainer container = new SimulationContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        container.loadSimulatorFromRes("simulation/init-properties/eventGenerator.json");
        for (int i = 0; i < 5; i++) {
            container.step();
        }
    }



}
