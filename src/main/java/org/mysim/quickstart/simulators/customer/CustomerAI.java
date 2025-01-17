package org.mysim.quickstart.simulators.customer;

import lombok.extern.slf4j.Slf4j;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class CustomerAI extends ResourceAI {
    @Override
    public void step() {
        SimulatorProperty property = getSimulatorProperty();
        if (property.getTurn() != 1) {
            return;
        }
        List<String> list = searchSimulatorByType("deliveryman");
        String deliverymanId = CommonUtils.getRandom(list);
        HashMap<Object, Object> context = new HashMap<>();
        context.put("deliveryman", deliverymanId);
        log.info("{}调度{}执行：delivery_demo", property.getSimulatorId(), deliverymanId);
        createBpmnProcess("delivery_demo", context);
    }
}
