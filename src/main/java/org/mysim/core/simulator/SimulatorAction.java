package org.mysim.core.simulator;

import org.mysim.core.message.SimMessage;

import java.util.Collection;
import java.util.List;

public interface SimulatorAction {


    String sendMessage(String targetId, SimMessage simMessage);

    String boardCastMessage(Collection<String> targetIds, SimMessage simMessage);

    SimMessage blockingReceive(String conversationId, long timeMills);

    List<SimMessage> blockingReceive(String conversationId, long timeMills, int num);

    void publishMessage();

    void deregister();


}
