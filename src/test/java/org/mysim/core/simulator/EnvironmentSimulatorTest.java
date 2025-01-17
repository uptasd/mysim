package org.mysim.core.simulator;

import com.fasterxml.jackson.core.type.TypeReference;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mysim.core.message.SimMessage;
import org.mysim.core.message.SystemMessageFactory;
import org.mysim.core.rt.container.SimulationContainer;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@Slf4j
class EnvironmentSimulatorTest {
    @Test
    public void testEnv(){
        EnvironmentSimulator environmentSimulator = buildEnvironmentSimulator("env1");
        ResourceSimulator publisher1 = buildPublisher("publisher1");
        ResourceSimulator subscriber1 = buildSubscriber("subscriber1");
        SimulationContainer container = new SimulationContainer();
        container.loadSimulator(environmentSimulator);
        container.loadSimulator(publisher1);
        container.loadSimulator(subscriber1);
        for (int i = 0; i < 4; i++) {
            container.step();
        }

    }

    private EnvironmentSimulator buildEnvironmentSimulator(String id) {
        return new EnvironmentSimulator(id);
    }

    private ResourceSimulator buildPublisher(String id) {
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId(id);
        ResourceAI ai = new ResourceAI() {
            @Override
            public void step() {
                SimulatorProperty property1 = getSimulatorProperty();
                if (property1.getTurn() == 0) {
                    Map<String, Object> args = new HashMap<>();
                    args.put("key1", "value1");
                    args.put("key2", "value2");
                    SimMessage simMessage = SystemMessageFactory.buildPublishContextMessage(args);
                    sendMessage("env1", simMessage);
                }

            }
        };
        return new ResourceSimulator(property, ai);
    }

    private ResourceSimulator buildSubscriber(String id) {
        SimulatorProperty property = new SimulatorProperty();
        property.setSimulatorId(id);
        ResourceAI ai = new ResourceAI() {
            @Override
            public void step() {
                SimulatorProperty property1 = getSimulatorProperty();
                if (property1.getTurn() == 3) {
                    Set<String> args = new HashSet<>();
                    args.add("key1");
                    args.add("key2");
                    SimMessage simMessage = SystemMessageFactory.buildPullContextMessage(args);
                    String sessionId = sendMessage("env1", simMessage);
                    SimMessage reply = blockingReceive(sessionId, 1000);
                    Map<String, Object> map = JsonUtils.jsonToObject(reply.getPayLoad(), new TypeReference<>() {
                    });
                    log.info("{}收到{}", getSimulatorProperty().getSimulatorId(), map);
                }
            }
        };
        ResourceSimulator simulator = new ResourceSimulator(property, ai);
        return simulator;
    }

}