package org.mysim.config;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CamundaConfig {
    public static final String gatewayAddress = "192.168.88.101:26500";
    public static final String bpmnFilePath = "simulation/bpmn/";
    public static final String ElasticSearchAddress = "192.168.88.101";
    public static final int ElasticSearchPort = 9200;
    public static final String scheme = "http";
    @Getter
    static ZeebeClient zeebeClient = ZeebeClient.newClientBuilder()
            .gatewayAddress(gatewayAddress)
            .usePlaintext()
            .build();
    @Getter
    static RestHighLevelClient restClient = createClient();

    private static RestHighLevelClient createClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(ElasticSearchAddress, ElasticSearchPort, scheme)));
    }





}
