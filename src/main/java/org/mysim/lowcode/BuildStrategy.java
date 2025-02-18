package org.mysim.lowcode;

import freemarker.template.TemplateException;
import org.mysim.lowcode.project.common.ResultBuilder;
import org.mysim.lowcode.project.controller.ModelControllerBuilder;
import org.mysim.lowcode.project.controller.QueryControllerBuilder;
import org.mysim.lowcode.project.service.QueryServiceBuilder;
import org.mysim.lowcode.project.service.SimulationServiceBuilder;
import org.mysim.lowcode.project.service.impl.QueryServiceImplBuilder;
import org.mysim.lowcode.project.service.impl.SimulationServiceImpBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuildStrategy {
    static Map<String, JavaFileBuilder> strategyMap = new HashMap<>();

    static {
        strategyMap.put("Result", new ResultBuilder());
        strategyMap.put("ModelController", new ModelControllerBuilder());
        strategyMap.put("SimulationService", new SimulationServiceBuilder());
        strategyMap.put("SimulationServiceImpl", new SimulationServiceImpBuilder());
        strategyMap.put("QueryService", new QueryServiceBuilder());
        strategyMap.put("QueryServiceImpl", new QueryServiceImplBuilder());
        strategyMap.put("QueryController", new QueryControllerBuilder());
    }

    public static void build(String fileName, String srcDir, String packageRoot) throws TemplateException, IOException {
        if (strategyMap.containsKey(fileName)) {
            strategyMap.get(fileName).build(srcDir, packageRoot);
        }
    }
}
