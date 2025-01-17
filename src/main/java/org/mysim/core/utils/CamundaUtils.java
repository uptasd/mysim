package org.mysim.core.utils;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.mysim.config.CamundaConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CamundaUtils {
    @Getter
    static ZeebeClient zeebeClient = CamundaConfig.getZeebeClient();
    @Getter
    static RestHighLevelClient restClient = CamundaConfig.getRestClient();
    public static void deployAllBpmnFiles() throws IOException {
        List<String> bpmnFiles = getAllBpmnFiles();

        if (bpmnFiles.isEmpty()) {
            log.warn("No BPMN files found in the resources/simulation/bpmn directory.");
            return;
        }
        for (String filename : bpmnFiles) {
            log.debug("deploying: {}", filename);
            var response = zeebeClient.newDeployResourceCommand()
                    .addResourceFromClasspath(CamundaConfig.bpmnFilePath + filename)
                    .send()
                    .join();
            log.debug("deploy done,response:{}", response);
        }

    }
    private static List<String> getAllBpmnFiles() throws IOException {
        List<String> fileNames = new ArrayList<>();
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:simulation/bpmn/*.bpmn");
        for (Resource resource : resources) {
            fileNames.add(resource.getFilename());
        }
        return fileNames;
    }

    public static long createNewInstance(String bpmnProcessId, Map<Object, Object> contexts) {
        ProcessInstanceEvent response = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId)
                .latestVersion()
                .variables(contexts)
                .send()
                .join();
        return response.getProcessInstanceKey();
    }

    public static void completeTask(ActivatedJob job, Map<String, String> output) {
        zeebeClient.newCompleteCommand(job.getKey())
                .variables(output)
                .send()
                .join();
    }

    public static void completeTask(long jobKey, Map<String, String> output) {
        zeebeClient.newCompleteCommand(jobKey)
                .variables(output)
                .send()
                .join();
    }

    public static void ThrowError(ActivatedJob job, String errorCode, String errorMsg) {
        zeebeClient.newThrowErrorCommand(job)
                .errorCode(errorCode)
                .errorMessage(errorMsg)
                .send()
                .join();
    }

    public static boolean isProcessComplete(String processInstanceKey) {
        RestHighLevelClient client = org.mysim.core.utils.CamundaUtils.getRestClient();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("value.processInstanceKey", processInstanceKey))
                .must(QueryBuilders.termQuery("value.bpmnElementType", "PROCESS"))
                .must(QueryBuilders.termQuery("intent", "ELEMENT_COMPLETED"))
        );
        SearchRequest searchRequest = new SearchRequest("zeebe-record-process-instance");
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            return hits != null && hits.length != 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
