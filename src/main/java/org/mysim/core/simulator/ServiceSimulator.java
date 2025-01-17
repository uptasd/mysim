package org.mysim.core.simulator;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import org.mysim.config.CamundaConfig;

import java.util.Map;

public abstract class ServiceSimulator extends Simulator {
    public ServiceSimulator(String simulatorId) {
        super(simulatorId);
        ZeebeClient client = CamundaConfig.getZeebeClient();
        JobWorker jobWorker = client.newWorker()
                .jobType(getSimulatorProperty().getSimulatorId())
                .handler(this::handle)
                .open();
    }

    private void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> input = job.getVariablesAsMap();
        Map<String, Object> output = doTask(job,input);
        client.newCompleteCommand(job)
                .variables(output)
                .send()
                .join();
    }


    public abstract Map<String, Object> doTask(ActivatedJob job,Map<String, Object> input);

    @Override
    public void deregister() {

    }
}
