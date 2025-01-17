package org.mysim.core.simulator.config;

import org.junit.jupiter.api.Test;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@SpringBootTest
class SimulatorFactoryTest {

    @Test
    void buildSimulator() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId("test");
        property.setSimulatorType("Type01");
        Simulator simulator = SimulatorFactory.buildSimulator(property, ResourceSimulator.class, ResourceAI.class);
        System.out.println(simulator);
    }

    @Test
    void loadSimulators() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Simulator> simulators = SimulatorFactory.loadSimulators("simulation/init-properties/Deliveryman.json");
        System.out.println(simulators);
    }

    @Test
    void testLoadFromConfig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println("here");
        List<Simulator> simulators = SimulatorFactory.loadSimulators("simulation/init-properties/Deliveryman.json");
        System.out.println(simulators);
    }
    @Test
    void testLoadAll(){
        List<Simulator> simulators = SimulatorFactory.loadAllRes("simulation/init-properties");
        System.out.println(simulators);

    }

}