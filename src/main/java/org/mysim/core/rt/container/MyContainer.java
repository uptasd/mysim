package org.mysim.core.rt.container;

import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.config.SimulatorFactory;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.StatusUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyContainer implements SimulationContainer {
    private final BaseContainer baseContainer;
    public static final String PROPERTY_KEY_PREFIX = "simulator-property:";

    public MyContainer() {
        baseContainer = new BaseContainer();
    }

    @Override
    public void step(long blockingMillTimes) {
        baseContainer.step(blockingMillTimes);
    }

    @Override
    public void step() {
        step(1000);
    }

    @Override
    public boolean loadSimulator(Simulator simulator) {
        if (!baseContainer.loadSimulator(simulator)) {
            return false;
        }
        SimulatorProperty property = simulator.getSimulatorProperty();
        StatusUtils.setProperty( property);
        return true;
    }

    @Override
    public void loadSimulatorFromRes(String resFileName) {
        List<Simulator> simulators = SimulatorFactory.loadSimulators(resFileName);
        for (Simulator simulator : simulators) {
            this.loadSimulator(simulator);
        }
    }

    @Override
    public void loadSimulatorFromDir(String restDir) {
        List<Simulator> simulators = SimulatorFactory.loadAllRes(restDir);
        for (Simulator simulator : simulators) {
            this.loadSimulator(simulator);
        }
    }

    @Override
    public List<Simulator> getAllSimulators() {
        return baseContainer.getAllSimulators();
    }
}
