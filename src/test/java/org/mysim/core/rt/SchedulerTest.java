package org.mysim.core.rt;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import jade.wrapper.StaleProxyException;
import org.junit.jupiter.api.Test;
import org.mysim.core.simulator.BpmnProcessProxy;
import org.mysim.core.events.action.external.ExternalEventActor;
import org.mysim.core.events.action.external.ExternalEventPayload;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SimMessageFactory;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.rt.scheduler.ConcurrentScheduler;
import org.mysim.core.rt.scheduler.SchedulerProperty;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.ServiceSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.config.ActionInfo;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.task.SimulationTask;
import org.mysim.core.utils.CamundaUtils;
import org.mysim.quickstart.simulators.deliveryman.actions.MoveToDeliveryPoint;
import org.mysim.quickstart.simulators.deliveryman.actions.MoveToRestaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class SchedulerTest {
    private static final Logger log = LoggerFactory.getLogger(SchedulerTest.class);

    @Test
    public void testSchedule() throws StaleProxyException, InterruptedException {
        SchedulerProperty property = new SchedulerProperty();
        property.setSimulatorId("ConcurrentScheduler");
        ConcurrentScheduler scheduler = new ConcurrentScheduler(property);
        Simulator simulator1 = buildTestSimulator("test01");
        Simulator simulator2 = buildTestSimulator("test02");
        SimulationContainer container = new SimulationContainer();
        container.loadSimulator(simulator1);
        container.loadSimulator(simulator2);
        container.loadSimulator(scheduler);
        scheduler.loadSimulator(simulator1);
        scheduler.loadSimulator(simulator2);
        scheduler.schedule();
        Thread.sleep(1000);
    }

    @Test
    public void testSchedule2() throws StaleProxyException {
        Simulator simulator1 = buildTestSimulator("test01");
        Simulator simulator2 = buildTestSimulator("test02");
        Simulator eventPublisher = buildEventPublisher("publisher");
        SimulationContainer container = new SimulationContainer();
        container.loadSimulator(simulator1);
        container.loadSimulator(simulator2);
        container.loadSimulator(eventPublisher);
        for (int i = 0; i < 5; i++) {
            container.step();
        }
        LocalDateTime time = container.getTime();
        log.info("{}", time);
    }

    @Test
    public void testBpmn() throws StaleProxyException, InterruptedException {
        Simulator simulator1 = buildTestSimulator("test01");
//        Simulator simulator2 = buildTestSimulator("test02");
//        Simulator simulator3 = buildTestSimulator("test03");
//        Simulator simulator4 = buildTestSimulator("test04");
//        Simulator simulator5 = buildTestSimulator("test05");
        Simulator customer1 = buildRequester("customer1");
        Simulator customer2 = buildRequester("customer2");
        Simulator customer3 = buildRequester("customer3");
        ServiceSimulator serviceSimulator = new ServiceSimulator("schedule-deliveryman") {
            @Override
            public Map<String, Object> doTask(ActivatedJob job, Map<String, Object> input) {
                Map<String, Object> mp = new HashMap<>();
                List<String> list = new ArrayList<>();
                list.add("test01");
//                list.add("test02");
//                list.add("test03");
//                list.add("test04");
                int idx = (int) (Math.random() * list.size());
                mp.put("deliveryman", list.get(idx));
                log.info("调度{}执行任务", list.get(idx));
                return mp;
            }
        };

        SimulationContainer container = new SimulationContainer();
        BpmnProcessProxy dispatcher = new BpmnProcessProxy("bpmnProcessProxyId");
        container.loadSimulator(simulator1);
//        container.loadSimulator(simulator2);
//        container.loadSimulator(simulator3);
//        container.loadSimulator(simulator4);
//        container.loadSimulator(simulator5);
        container.loadSimulator(serviceSimulator);
        container.loadSimulator(customer1);
        container.loadSimulator(customer2);
        container.loadSimulator(customer3);
        container.loadSimulator(dispatcher);
//        Thread.sleep(3000);
        for (int i = 0; i < 10; i++) {
            container.step(1000);
        }
    }

    @Test
    public void testTmp() throws IOException {
        String id = "2251799814506461";
        boolean processComplete = CamundaUtils.isProcessComplete(id);
        System.out.println(processComplete);
    }

    private Simulator buildTestSimulator(String id) {
        ResourceAI ai = new ResourceAI() {
            @Override
            public Collection<SimulationTask> planning(Collection<SimulationTask> tasks) {
                SimulatorProperty property = getSimulatorProperty();
                log.info("{} current task:{}", property.getSimulatorId(), tasks);
                Collection<SimulationTask> ret = super.planning(tasks);
                log.info("{} execute task:{}", property.getSimulatorId(), ret);
                return ret;
            }
        };
        SimulatorProperty property = new SimulatorProperty();
        ResourceSimulator resourceSimulator = new ResourceSimulator(property, ai);

        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setActionName("MoveToRestaurant");
        actionInfo.setActionClass(MoveToRestaurant.class.getName());
        ai.registerBpmnActorInfo("MoveToRestaurant", actionInfo);

        ActionInfo actionInfo2 = new ActionInfo();
        actionInfo2.setActionName("MoveToDeliveryPoint");
        actionInfo2.setActionClass(MoveToDeliveryPoint.class.getName());
        ai.registerBpmnActorInfo("MoveToDeliveryPoint", actionInfo2);

        property.setSimulatorId(id);
        ai.registerExternalActor("badWeather", new ExternalEventActor(ai) {
            @Override
            public void onActivated(Map<String, Object> args) {
                log.info("{} 感知到事件发生:坏天气,args:{}", getSimulatorProperty().getSimulatorId(), args);
            }

            @Override
            public void onFinished(Map<String, Object> args) {
                log.info("{} 感知到事件结束:坏天气,args:{}", getSimulatorProperty().getSimulatorId(), args);

            }
        });
        return resourceSimulator;
    }


    private Simulator buildEventPublisher(String id) {
        SimulatorAI ai = new ResourceAI() {
            @Override
            public void step() {
                super.step();
                long turn = getSimulatorProperty().getTurn();
                ExternalEventPayload externalEventPayload = new ExternalEventPayload();
                List<String> list = new ArrayList<>();
                list.add("test01");
                list.add("test02");
                if (turn == 1) {
                    externalEventPayload.setEventFlag(0);
                    Map<String, Object> args = new HashMap<>();
                    args.put("key", "value");
                    externalEventPayload.setArgs(args);
                    SimMessage message = SimMessageFactory.buildExternalEventMessage("badWeather", externalEventPayload);
                    boardCastMessage(list, message);
                } else if (turn == 4) {
//                    externalEventContext.setEventFlag(1);
//                    SimMessage message = SimMessageFactory.buildExternalEventMessage("badWeather", externalEventContext);
//                    boardCastMessage(list, message);
                }
            }
        };
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId(id);
        Simulator simulator = new ResourceSimulator(property, ai);
        return simulator;
    }

    private Simulator buildRequester(String id) {
        SimulatorAI ai = new ResourceAI() {
            @Override
            public void step() {
                long turn = getSimulatorProperty().getTurn();
                String id = getSimulatorProperty().getSimulatorId();
                if (turn == 1) {
                    log.info("{}开启流程{}", id, "delivery_demo");
                    Map<Object, Object> input = new HashMap<>();
                    input.put("deliveryman", "test01");
                    createBpmnProcess("delivery_demo", input);
//                    CamundaUtils.createNewInstance("delivery_demo", input);
                }
            }
        };
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId(id);
        Simulator simulator = new ResourceSimulator(property, ai);
        return simulator;
    }
}
