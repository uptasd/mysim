package org.mysim.lowcode;

import freemarker.template.TemplateException;
import org.mysim.lowcode.utils.FreeMakerUtils;

import java.io.IOException;
import java.util.Map;

public abstract class JavaFileBuilder {
    protected abstract void build(String srcDir, String packageRoot) throws TemplateException, IOException;

    protected void buildFile(String templateDir, String templateName, String outputFileNamePath, Map<String, Object> dataModel) throws TemplateException, IOException {
        FreeMakerUtils.buildFile(templateDir, templateName, outputFileNamePath, dataModel);
    }
}
