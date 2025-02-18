package org.mysim.lowcode;

import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.mysim.lowcode.utils.ProjectUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ProjectBuildTest {
    @Test
    void testBuildEmptyProject() throws IOException {
        ProjectUtils.buildEmptyProject("org.test","demo01","0.0.1","./tmp");
    }
    @Test
    void testBuildSimulationProject() throws IOException, TemplateException {
        ProjectUtils.buildSimulationProject("demo03","./tmp");
    }
    @Test
    void runProject() throws Exception {
        ProjectUtils.runProject("./tmp/demo01");
    }


}
