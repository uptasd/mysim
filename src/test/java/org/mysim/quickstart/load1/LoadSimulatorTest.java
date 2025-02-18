package org.mysim.quickstart.load1;

import org.junit.jupiter.api.Test;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
@SpringBootTest
public class LoadSimulatorTest {
    /*
     * 手动加载
     * */
    @Test
    void loadSimulator() {
        BaseContainer container = new BaseContainer();
        Simulator simulator = new ResourceSimulator("testSimulator");
        ResourceAI ai = new ResourceAI(simulator);
        ai.registerExternalActor("test", new ExternalEventActor(ai) {
            @Override
            public void onActivated(Map<String, Object> args) {

            }

            @Override
            public void onFinished(Map<String, Object> args) {

            }
        });
        container.loadSimulator(simulator);
        Map<String, List<String>> allActorId = simulator.getAllActorId();
    }

    /*
    * 用配置文件加载，以属性为核心，读取一个属性配置文件，实例化后创建对应一个仿真体实例
    * 仿真体的AI、动作等由simulation/config/simulator-config.yaml给出
    * 注意：创建的simulator类必须包含如下的构造函数，否则报错
    * public Deliveryman(SimulatorProperty simulatorProperty, SimulatorAI simulatorAI) {
         super(simulatorProperty, simulatorAI);
     }
     *
    * */
    @Test
    void loadSimulatorFromRes() {
        BaseContainer container = new BaseContainer();
        //属性配置文件存放在resource/simulation/init-properties/
        container.loadSimulatorFromRes("simulation/init-properties/Deliveryman.json");
        List<Simulator> simulators = container.getAllSimulators();
        System.out.println(simulators);
        for (Simulator simulator : simulators) {
            String id = simulator.getSimulatorProperty().getSimulatorId();
            System.out.println(id+"包含动作："+simulator.getAllActorId());

        }
    }

    /*
     * 读取simulation/init-properties目录下的所有json配置文件
     * */
    @Test
    void loadSimulatorFromDir() {
        BaseContainer container = new BaseContainer();
        container.loadSimulatorFromDir("simulation/init-properties");
        List<Simulator> simulators = container.getAllSimulators();
        System.out.println(simulators);
        for (Simulator simulator : simulators) {
            String id = simulator.getSimulatorProperty().getSimulatorId();
            System.out.println(id+"包含动作："+simulator.getAllActorId());
        }
    }

}
