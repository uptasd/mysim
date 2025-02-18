package org.mysim.core.simulator;

import org.mysim.core.message.SimMessage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SimulatorAction {


    String sendMessage(String targetId, SimMessage simMessage);

    String boardCastMessage(Collection<String> targetIds, SimMessage simMessage);

    SimMessage blockingReceive(String conversationId, long timeMills);

    public List<SimMessage> blockingReceive(String conversationId, long timeMills, Set<String> expectedIds);

    List<SimMessage> blockingReceive(String conversationId, long timeMills, int num);

    void publishMessage();

    void deregister();


}
