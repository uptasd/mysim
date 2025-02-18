package org.mysim.core.simulator.status;

import jade.wrapper.StaleProxyException;
import org.junit.jupiter.api.Test;
import org.mysim.core.events.action.external.ExternalEventPayload;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SimMessageFactory;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestTmp {
    @Test
    public void test01() throws StaleProxyException {
        Simulator simulator1 = buildRes("test01");
        Simulator simulator2 = buildEventPublisher("BadWeather-publisher");
        BaseContainer container = new BaseContainer();
        container.loadSimulator(simulator1);
        container.loadSimulator(simulator2);
        for (int i = 0; i < 5; i++) {
            container.step(100);
        }

    }

    private Simulator buildRes(String id) {
        TestProperty property = new TestProperty();

        property.setSimulatorId(id);
        property.setSimulatorType("SimulatorType1");
        property.setSpeed(50);
        ResourceAI ai = new ResourceAI();
        Simulator simulator = new ResourceSimulator(property, ai);
        ai.registerExternalActor("badWeather", new BadWeather(ai));
        return simulator;
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
                } else if (turn == 3) {
                    externalEventPayload.setEventFlag(1);
                    SimMessage message = SimMessageFactory.buildExternalEventMessage("badWeather", externalEventPayload);
                    boardCastMessage(list, message);
                }
            }
        };
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId(id);
        Simulator simulator = new ResourceSimulator(property, ai);
        return simulator;
    }
}
