package org.mysim.core.rt.container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mysim.config.SimulationConfig;
import org.mysim.core.rt.scheduler.*;
import org.mysim.core.simulator.BpmnProcessProxy;
import org.mysim.core.simulator.SearchSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.config.SimulatorFactory;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.CamundaUtils;
import org.mysim.core.utils.TimeUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimulationContainer {
    public static ContainerController cc;
    SimulationConfig simulationConfig;
    @Getter
    HashMap<String, Scheduler> schedulers = new HashMap<>();
    Map<String, Simulator> simulators = new ConcurrentHashMap<>();
    // 类型：[角色Id,角色Id,...]
    Map<String, Set<String>> simulatorTypes = new ConcurrentHashMap<>();
    private final AnnotationProcessor annotationProcessor = new AnnotationProcessor(this);
    private ConcurrentScheduler defaultScheduler;
    private SearchSimulator simulatorSearcher;
    private BpmnProcessProxy bpmnProcessProxy;

    public static final String simulatorSearcherId = "simulatorSearcher";
    public static final String bpmnProxySimulatorId = "bpmnProcessProxy";
    public static final String defaultSchedulerId = "defaultScheduler";


    private void deployBpmn() {
        try {
            log.info("开始部署bpmn文件");
            CamundaUtils.deployAllBpmnFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSimulators() {
        defaultScheduler = SchedulerFactory.buildConCurrentScheduler("DefaultScheduler", 0);
        simulatorSearcher = new SearchSimulator(simulatorSearcherId);
        bpmnProcessProxy = new BpmnProcessProxy(bpmnProxySimulatorId);
        loadScheduler(defaultScheduler);
        loadSimulator(simulatorSearcher);
        loadSimulator(bpmnProcessProxy);
    }

    public SimulationContainer() {
        simulationConfig = SimulationConfig.getInstance();
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "false");
        cc = rt.createMainContainer(p);
        initSimulators();
        deployBpmn();
    }

    public synchronized void loadSimulatorFromRes(String resFileName) {
        List<Simulator> simulators = SimulatorFactory.loadSimulators(resFileName);
        for (Simulator simulator : simulators) {
            loadSimulator(simulator);
        }
    }

    public synchronized void loadSimulatorFromDir(String path) {
        List<Simulator> simulators = SimulatorFactory.loadAllRes(path);
        for (Simulator simulator : simulators) {
            loadSimulator(simulator);
        }
    }

    public synchronized void loadSimulator(Simulator simulator) {
        SimulatorProperty property = simulator.getSimulatorProperty();
        if (property == null || property.getSimulatorId() == null) {
            log.error("{} 缺少ID,加载失败", simulator);
            return;
        }
        annotationProcessor.process(simulator);
        if (property.getSchedulerId() == null) {
            property.setSchedulerId("DefaultScheduler");
            defaultScheduler.loadSimulator(simulator);
        }
        String simulatorId = property.getSimulatorId();
        String simulatorType = property.getSimulatorType();
        if (simulatorType == null) simulatorType = "UNKNOWN";
        try {
            AgentController ac = cc.acceptNewAgent(simulatorId, simulator.getAgent());
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
        simulator.setContainer(this);
        simulators.put(simulatorId, simulator);
        Set<String> sets = simulatorTypes.getOrDefault(simulatorType, new HashSet<>());
        sets.add(simulatorId);
        simulatorTypes.put(simulatorType, sets);
        log.debug("{} 已成功加载", simulatorId);
    }

    public void step(long timeInterval) {
        long turn = simulationConfig.getTurn();
        log.info("--------------turn:{}--------------", turn++);
        ArrayList<Scheduler> schedulerList = new ArrayList<>(schedulers.values());
        schedulerList.sort(Comparator.comparingInt(Scheduler::getPriority));
        schedule(schedulerList, timeInterval);
        syncTurn(schedulerList, turn);
        simulationConfig.setTurn(turn);
    }

    public void step() {
        step(500);
    }

    private void schedule(List<Scheduler> schedulers, long timeInterval) {
        long blockTime = timeInterval / schedulers.size();
        for (Scheduler scheduler : schedulers) {
            log.info("--------------唤醒调度器:{}--------------", scheduler.getSchedulerId());
            scheduler.schedule();
            try {
                Thread.sleep(blockTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void syncTurn(List<Scheduler> schedulers, long turn) {
        for (Scheduler scheduler : schedulers) {
            log.info("--------------同步调度器:{}--------------", scheduler.getSchedulerId());
            scheduler.syncTime(turn);
        }
    }

    public void loadScheduler(Scheduler scheduler) {
        if (schedulers.containsKey(scheduler.getSchedulerId())) {
            return;
        }
        schedulers.put(scheduler.getSchedulerId(), scheduler);
        scheduler.setSimulationContainer(this);
        String simulatorId = scheduler.getSchedulerId();
        try {
            AgentController ac = cc.acceptNewAgent(simulatorId, scheduler.getAgent());
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean containsScheduler(String schedulerId) {
        return schedulers.containsKey(schedulerId);
    }

    public long getTurn() {
        return simulationConfig.getTurn();
    }

    public String getSimulatorSearcherId() {
        return simulatorSearcherId;
    }

    public String getBpmnProxySimulatorId() {
        return bpmnProxySimulatorId;
    }

    public LocalDateTime getTime() {
        return TimeUtils.getLogicTime(getTurn());
    }

    public List<Simulator> getAllSimulators() {
        return simulators.values().stream().toList();
    }


    public List<String> getSimulatorIdByType(String type) {
        return simulatorTypes.getOrDefault(type, new HashSet<>()).stream().toList();
    }
}
