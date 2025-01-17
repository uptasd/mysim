package org.mysim.core.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimMessage {
    private long turn;
    private String senderId;
    private String eventType;//CUSTOM or SYSTEM
    private String eventName;
    private String sessionId;
    private String payLoad;


    public SimMessage buildReply(long turn, String senderId, String payLoad) {
        SimMessage reply = new SimMessage();
        reply.setTurn(turn);
        reply.setSenderId(senderId);
        reply.setEventType(this.eventType);
        reply.setSessionId(this.sessionId);
        reply.setPayLoad(payLoad);
        return reply;
    }

    public SimMessage buildReply(SimulatorAI ai, String payload) {
        SimulatorProperty property = ai.getSimulatorProperty();
        return buildReply(property.getTurn(), property.getSimulatorId(), payload);
    }

}
