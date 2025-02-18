package org.mysim.core.rt.container;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.mysim.core.log.ActionLog;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.config.SimulatorFactory;
import org.mysim.core.simulator.status.SimulatorProperty;

import java.util.List;

@Slf4j
public class JadeContainer implements SimulationContainer {
    public static ContainerController cc;

    public JadeContainer() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "false");
        cc = rt.createMainContainer(p);
    }

    @Override
    public void step(long blockingTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void step() {
        step(1000);
    }

    @Override
    public synchronized boolean loadSimulator(Simulator simulator) {
        SimulatorProperty property = simulator.getSimulatorProperty();
        if (property == null || property.getSimulatorId() == null) {
            log.error("{} 缺少ID,加载失败", simulator);
            return false;
        }
        String simulatorId = property.getSimulatorId();
        try {
            AgentController ac = cc.acceptNewAgent(simulatorId, simulator.getAgent());
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public synchronized boolean loadAgent(String agentId, Agent agent) {
        try {
            AgentController ac = cc.acceptNewAgent(agentId, agent);
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public synchronized void loadSimulatorFromRes(String resFileName) {
        List<Simulator> simulators = SimulatorFactory.loadSimulators(resFileName);
        for (Simulator simulator : simulators) {
            loadSimulator(simulator);
        }
    }

    @Override
    public synchronized void loadSimulatorFromDir(String restDir) {
        List<Simulator> simulators = SimulatorFactory.loadAllRes(restDir);
        for (Simulator simulator : simulators) {
            loadSimulator(simulator);
        }
    }

    @Override
    public List<Simulator> getAllSimulators() {
        throw new UnsupportedOperationException();
    }

}
