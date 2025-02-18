package org.mysim.lowcode.project.service.impl;

import freemarker.template.TemplateException;
import org.mysim.lowcode.JavaFileBuilder;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimulationServiceImpBuilder extends JavaFileBuilder {
    @Override
    protected void build(String srcDir, String packageRoot) throws TemplateException, IOException {
        String serviceImplFileDir = srcDir + "/service/impl";
        String serviceImplPackageName = packageRoot + ".service.impl";
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", serviceImplPackageName);
        dataModel.put("SimulationServicePackageName", packageRoot + ".service.SimulationService");
        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.SIMULATION_SERVICE_IMPL_FILE_NAME, serviceImplFileDir + "/SimulationServiceImpl.java", dataModel);

    }
}
