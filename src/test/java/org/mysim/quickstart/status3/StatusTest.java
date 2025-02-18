package org.mysim.quickstart.status3;

import org.junit.jupiter.api.Test;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.Simulator;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StatusTest {
    // 外部事件测试+状态转移测试
    // 恶劣天气生成器会生成事件并发送到所有外卖骑手，骑手会响应这个事件，减少自身的速度
    // 速度值的变化会改变自身状态->（高速、低速、正常）
    // 属性配置文件：simulation/config/state_rules.yaml
    // 事件响应动作类：org.mysim.quickstart.simulators.deliveryman.actions.HandleBadWeather
    // (这些动作类需要在simulator-config.yaml中配置)
    // 注意1：在application.yml配置action-log.auto-log为true后会自动生成动作日志，可以在表action_log查看
    // 注意2：如果没有启动mysql，并且设置自动保存日志到mysql会出现超时错误
    @Test
    public void testExternalEvent() {
        BaseContainer container = new BaseContainer();
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        container.loadSimulatorFromRes("simulation/init-properties/eventGenerator.json");
        List<Simulator> allSimulators = container.getAllSimulators();
        for (int i = 0; i < 5; i++) {
            container.step();
        }
    }


}
