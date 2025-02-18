package org.mysim.core.simulator.status;

import org.junit.jupiter.api.Test;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.config.ActionInfo;
import org.mysim.core.simulator.config.RuleFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatusManagerTest {
    @Test
    void testSimulator() throws  InterruptedException {
        RuleFactory.loadConfig("simulation/config/state_rules_tmp.yaml");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TestProperty testProperty = new TestProperty();
        testProperty.setHealth(5);
        testProperty.setSimulatorId("sender");
        testProperty.setSimulatorType("SimulatorType1");

        TestProperty2 testProperty2 = new TestProperty2();
        testProperty2.setHealth(24);
        testProperty2.setSimulatorId("receiver");
        testProperty2.setSimulatorType("SimulatorType2");
        SimulatorAI senderAI = new ResourceAI() {
            @Override
            public void step() {
                SimMessage simMessage = new SimMessage();
                simMessage.setEventName("attack");
                Simulator simulator = getSimulator();
                boolean isReplied = simulator.getMessageProxy().sendAndWaitAck("receiver", simMessage, 1000);
                if (isReplied) {
                    TestProperty property = (TestProperty) getSimulatorProperty();
                    int health = property.getHealth();
                    property.setHealth(health + 5);
//                    System.out.println("restore heath:" + health + "->" + property.getHealth());
                    StatusManager.checkAndUpdateStates(getSimulatorProperty(), getSimulatorStatus());
                    countDownLatch.countDown();
                }
            }
        };
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setActionClass(Attack.class.getName());
        actionInfo.setActionName("attack");
        ResourceAI receiverAI = new ResourceAI();
        receiverAI.registerBpmnActorInfo("attack", actionInfo);
        Simulator sender = new ResourceSimulator(testProperty, senderAI);
        Simulator receiver = new ResourceSimulator(testProperty2, receiverAI);
        BaseContainer baseContainer = new BaseContainer();
        baseContainer.loadSimulator(sender);
        baseContainer.loadSimulator(receiver);
        sender.getSimulatorStatus().getAllStatus();
        senderAI.step();
        boolean messageReceived = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(messageReceived, "A和B以完成信息交互");
        assertTrue(sender.getSimulatorStatus().containsStatus("HighHealth"), "sender状态自动变为HighHealth");
        assertTrue(receiver.getSimulatorStatus().containsStatus("LowHealth"), "receiver状态自动变为LowHealth");
    }

}

