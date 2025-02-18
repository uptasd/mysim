package org.mysim.lowcode.project.common;

import freemarker.template.TemplateException;
import org.mysim.lowcode.JavaFileBuilder;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultBuilder extends JavaFileBuilder {
    @Override
    protected void build(String srcDir, String packageRoot) throws TemplateException, IOException {
        String CommonPath = srcDir + "/common";
        String commonPackageName = packageRoot + ".common";
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", commonPackageName);
        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.RESULT_FTL_FILE_NAME, CommonPath + "/Result.java", dataModel);

    }
}
