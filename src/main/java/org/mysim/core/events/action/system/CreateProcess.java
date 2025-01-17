package org.mysim.core.events.action.system;

import org.mysim.core.events.action.ActionContext;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.BpmnProcessProxyAI;
import org.mysim.core.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateProcess extends SystemActor {
    private static final Logger log = LoggerFactory.getLogger(CreateProcess.class);
    BpmnProcessProxyAI bpmnProcessProxyAI;

    public CreateProcess(BpmnProcessProxyAI bpmnProcessProxyAI) {
        super(bpmnProcessProxyAI);
        this.bpmnProcessProxyAI = bpmnProcessProxyAI;
    }

    @Override
    public void execute(ActionContext context) {
        SimMessage eventMessage = getSimMessage();
        String payLoad = eventMessage.getPayLoad();
        CreateProcessPayLoad createProcessPayLoad = JsonUtils.jsonToObject(payLoad, CreateProcessPayLoad.class);
        String processId = createProcessPayLoad.getProcessId();
        if (processId == null || processId.isEmpty()) {
            log.error("create process fail,reason: processId should not null,msg from {}", eventMessage.getSenderId());
            return;
        }
        log.debug("creating process instance:{},msg from:{}", processId, eventMessage.getSenderId());
        bpmnProcessProxyAI.createInstance(processId, createProcessPayLoad.getContexts());
    }

}
