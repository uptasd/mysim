package org.mysim.core.events.listener;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.mysim.core.events.EventType;
import org.mysim.core.events.SystemEvents;

public class MessageTemplateFactory {
    static MessageTemplate meta = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    public static MessageTemplate buildSystemEventMT() {
        return buildMT(EventType.SYSTEM_EVENT.name());
    }
    public static MessageTemplate buildBPMNEventMT() {
        return buildMT(EventType.BPMN_EVENT.name());
    }
    public static MessageTemplate buildExternalEventMT() {
        return buildMT(EventType.EXTERNAL_EVENT.name());
    }

    public static MessageTemplate buildMT(String conversationId) {
        return MessageTemplate.and(meta, MessageTemplate.MatchConversationId(conversationId));
    }
}
