package org.mysim.lowcode.project.controller;

import freemarker.template.TemplateException;
import org.mysim.lowcode.JavaFileBuilder;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModelControllerBuilder extends JavaFileBuilder {
    @Override
    public void build(String srcRoot, String packageRoot) throws TemplateException, IOException {
        String controllerFileDir = srcRoot + "/controller";
        String controllerPackageName = packageRoot + ".controller";
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", controllerPackageName);
        dataModel.put("commonPackage", packageRoot + ".common");
        dataModel.put("SimulationServicePackage", packageRoot + ".service");
        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.MODEL_CONTROLLER_FTL_FILE_NAME, controllerFileDir + "/ModelController.java", dataModel);

    }
}
