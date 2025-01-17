package org.mysim.core.bpmn;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TestService {
    ZeebeClient client = ZeebeClient.newClientBuilder()
            .gatewayAddress("192.168.88.101:26500")
            .usePlaintext()
            .build();

    @Test
    public void test01() throws InterruptedException {
        JobWorker jobWorker = client.newWorker()
                .jobType("nurse-schedule")
                .handler((client1, job) -> {
                    Map<String, Object> variablesAsMap = job.getVariablesAsMap();
                    Map<String, String> output = new HashMap<>();
                    output.put("nurse", "AAA");
                    client.newCompleteCommand(job.getKey())
                            .variables(output)
                            .send()
                            .join();

                }).open();

        Thread.sleep(300);
    }

    @Test
    public void test02() throws InterruptedException {
        JobWorker jobWorker2 = client.newWorker()
                .jobType("io.camunda.zeebe:userTask")
                .handler((client1, job) -> {
                    Map<String, Object> variablesAsMap = job.getVariablesAsMap();
                    Map<String, String> output = new HashMap<>();
//                    output.put("nurse", "AAA");
                    client.newCompleteCommand(job.getKey())
                            .variables(output)
                            .send()
                            .join();

                }).open();
        while (true) ;
    }

    @Test
    public void test03() throws InterruptedException {
        JobWorker nurse_scheduler = client.newWorker()
                .jobType("nurse-schedule")
                .handler((client1, job) -> {
                    Map<String, Object> variablesAsMap = job.getVariablesAsMap();
                    Map<String, String> output = new HashMap<>();
                    output.put("nurse", "AAA");
                    client.newCompleteCommand(job.getKey())
                            .variables(output)
                            .send()
                            .join();

                }).open();
        JobWorker docker_scheduler = client.newWorker()
                .jobType("docker-schedule")
                .handler((client1, job) -> {
                    Map<String, Object> variablesAsMap = job.getVariablesAsMap();
                    Map<String, String> output = new HashMap<>();
                    output.put("docker", "BBB");
                    client.newCompleteCommand(job.getKey())
                            .variables(output)
                            .send()
                            .join();

                }).open();
        while (true) ;
    }

}
