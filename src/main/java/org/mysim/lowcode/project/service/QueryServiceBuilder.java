package org.mysim.lowcode.project.service;

import freemarker.template.TemplateException;
import org.mysim.lowcode.JavaFileBuilder;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QueryServiceBuilder extends JavaFileBuilder {
    @Override
    protected void build(String srcDir, String packageRoot) throws TemplateException, IOException {
        String serviceFileDir = srcDir + "/service";
        String servicePackageName = packageRoot + ".service";
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", servicePackageName);

        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.QUERY_SERVICE_FILE_NAME, serviceFileDir+"/QueryService.java", dataModel);
    }
}
