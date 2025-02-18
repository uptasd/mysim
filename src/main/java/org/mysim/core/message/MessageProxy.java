package org.mysim.core.message;


import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.mysim.core.events.EventType;
import org.mysim.core.events.listener.MessageTemplateFactory;
import org.mysim.core.exception.MessageTimeoutException;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MessageProxy {
    private static final Logger log = LoggerFactory.getLogger(MessageProxy.class);
    SimulatorAgent agent;
    SimulatorProperty property;

    public MessageProxy(SimulatorAgent agent) {
        this.agent = agent;
        Simulator simulator = agent.getSimulator();
        property = simulator.getSimulatorProperty();
    }

    //if sessionId is null,set ConversationId = simMessage.EventType
    //else if not null,set ConversationId = sessionId
    //eventType = SYSTEM,will receive by SystemEventListener,and dispatch by eventName(such step、sync_time)
    //eventType = BPMN,will receive by BPMNEventListener,and dispatch by eventName(user defined)
    //eventType = EXTERNAL,will receive by BPMNEventListener,and dispatch by eventName(user defined)
    public String send(String targetId, SimMessage simMessage) {
        checkSimMessage(simMessage);
        ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
        aclMessage.addReceiver(new AID(targetId, AID.ISLOCALNAME));
        String sessionId = simMessage.getSessionId();
        if (sessionId != null) {
            aclMessage.setConversationId(simMessage.getSessionId());
        } else {
            sessionId = buildConversationId(simMessage.getSenderId(), targetId);
            simMessage.setSessionId(sessionId);
            aclMessage.setConversationId(simMessage.getEventType());
        }
        aclMessage.setContent(JsonUtils.objectToJson(simMessage));
        agent.send(aclMessage);
        return sessionId;
    }

    public String boardCast(Collection<String> targetIds, SimMessage simMessage) {
        checkSimMessage(simMessage);
        String sessionId = simMessage.getSessionId();
        String conversationId = sessionId;
        if (sessionId == null) {
            sessionId = buildConversationId(simMessage.getSenderId(), targetIds);
            simMessage.setSessionId(sessionId);
            conversationId = simMessage.getEventType();
        }
        ACLMessage aclMessage = buildMessage(targetIds, conversationId, simMessage);
        agent.send(aclMessage);
        return sessionId;

    }

    private ACLMessage buildMessage(Collection<String> targetIds, String conversationId, SimMessage simMessage) {
        ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
        for (String targetId : targetIds) {
            aclMessage.addReceiver(new AID(targetId, AID.ISLOCALNAME));
        }
        aclMessage.setConversationId(conversationId);
        aclMessage.setContent(JsonUtils.objectToJson(simMessage));
        return aclMessage;
    }

    private void checkSimMessage(SimMessage simMessage) {
        String sender = simMessage.getSenderId();
        if (sender == null || sender.isEmpty()) {
            simMessage.setSenderId(property.getSimulatorId());
        }

        String eventType = simMessage.getEventType();
        if (eventType == null || eventType.isEmpty()) {
            simMessage.setEventType(EventType.BPMN_EVENT.name());
        }

        simMessage.setTurn(property.getTurn());
    }

    public boolean sendAndWaitAck(String targetId, SimMessage message, long blockingTimeMills) {
        //todo
        String sessionId = send(targetId, message);
//        log.info("{} send msg:{}", property.getSimulatorId(), message);
        try {
            blockingReceive(sessionId, blockingTimeMills);
            return true;
        } catch (MessageTimeoutException e) {
            return false;
        }
    }


    public boolean boardCastAndWaitAck(Collection<String> targetIds, String conversationId) {
        //todo
        return false;
    }

    public SimMessage blockingReceive(String conversationId, long blockingTimeMills) throws MessageTimeoutException {
        //todo
        MessageTemplate mt = MessageTemplateFactory.buildMT(conversationId);
        ACLMessage message = agent.blockingReceive(mt, blockingTimeMills);
        if (message == null) {
            String timeoutMsg = String.format("%s blocking receive time out", property.getSimulatorId());
            throw new MessageTimeoutException(timeoutMsg);
        }
        return JsonUtils.jsonToObject(message.getContent(), SimMessage.class);
    }

    public List<SimMessage> blockingReceive(String conversationId, long blockingTimeMills, Set<String> expectedIds) {
        MessageTemplate mt = MessageTemplateFactory.buildMT(conversationId);
        long startTime = System.currentTimeMillis();
        HashSet<String> full = new HashSet<>(expectedIds);
        List<SimMessage> replyMessages = new ArrayList<>();
        while (!expectedIds.isEmpty()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = blockingTimeMills - elapsedTime;
            if (remainingTime <= 0) {
                String timeoutMsg = String.format(
                        "%s blocking receive time out while waiting for %d messages, not received %s",
                        property.getSimulatorId(), full.size(), expectedIds
                );
                throw new MessageTimeoutException(timeoutMsg);
            }
            ACLMessage message = agent.blockingReceive(mt, blockingTimeMills);
            if (message == null) {

                String timeoutMsg = String.format(
                        "%s blocking time out (%s mills) for waiting %d messages,but received %d,{%s} not replied",
                        property.getSimulatorId(), blockingTimeMills, full.size(), replyMessages.size(), expectedIds
                );
                throw new MessageTimeoutException(timeoutMsg);
            }

            SimMessage simMessage = JsonUtils.jsonToObject(message.getContent(), SimMessage.class);
            expectedIds.remove(simMessage.getSenderId());
            replyMessages.add(simMessage);
        }
        return replyMessages;
    }

    public List<SimMessage> blockingReceive(String conversationId, long blockingTimeMills, int num) throws MessageTimeoutException {
        MessageTemplate mt = MessageTemplateFactory.buildMT(conversationId);
        long startTime = System.currentTimeMillis();
        List<SimMessage> replyMessages = new ArrayList<>();
        while (replyMessages.size() < num) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = blockingTimeMills - elapsedTime;
            if (remainingTime <= 0) {
                String timeoutMsg = String.format(
                        "%s blocking receive time out while waiting for %d messages, received %d",
                        property.getSimulatorId(), num, replyMessages.size()
                );
                throw new MessageTimeoutException(timeoutMsg);
            }
            ACLMessage message = agent.blockingReceive(mt, blockingTimeMills);
            if (message == null) {

                String timeoutMsg = String.format(
                        "%s blocking time out (%s mills) for waiting %d messages,but received %d",
                        property.getSimulatorId(), blockingTimeMills, num, replyMessages.size()
                );
                throw new MessageTimeoutException(timeoutMsg);
            }
            SimMessage simMessage = JsonUtils.jsonToObject(message.getContent(), SimMessage.class);
            replyMessages.add(simMessage);
        }
        return replyMessages;
    }

    @Deprecated
    public void publish(String envId, String topicId, SimMessage simMessage) {


    }

    private String buildConversationId(String sender, String receiver) {
        return buildConversationId(sender, List.of(receiver));
    }

    private String buildConversationId(String sender, Collection<String> receivers) {
        String uuid = UUID.randomUUID().toString();
        StringBuilder prefix = new StringBuilder(sender);
        for (String receiver : receivers) {
            prefix.append("-").append(receiver);
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(prefix.toString().getBytes());
            String signData = new BigInteger(1, md5.digest()).toString(16);
            return signData + "-" + uuid;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
