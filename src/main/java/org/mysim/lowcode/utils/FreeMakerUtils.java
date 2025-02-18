package org.mysim.lowcode.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreeMakerUtils {

    public static void buildPomFile(String artifactId, String outputFileNamePath) throws TemplateException, IOException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("artifactId", artifactId);
        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.POM_FTL_FILE_NAME, outputFileNamePath, dataModel);
    }

    public static void buildApplicationFile(Map<String, Object> model, String outputFileNamePath) throws TemplateException, IOException {
        Map<String, Object> dataModel = new HashMap<>();
        //默认数值
        dataModel.put("host", "192.168.88.101");
        dataModel.put("port", "6379");
        dataModel.put("redis_pwd", "123456");
        dataModel.put("databse", "2");
        dataModel.put("datasource_url", "jdbc:mysql://192.168.88.101:3306/tmp?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true");
        dataModel.put("datasource_user_name", "root");
        dataModel.put("datasource_user_pwd", "123");
        if (model != null) {
            dataModel.putAll(model);
        }
        buildFile(ProjectMetaConfig.BACKEND_FILE_DIR, ProjectMetaConfig.APPLICATION_FTL_FILE_NAME, outputFileNamePath, dataModel);
    }

    public static void buildFile(String templateDir, String templateName, String outputFileNamePath, Map<String, Object> dataModel) throws IOException, TemplateException {

        // 配置 FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(templateDir)); // 设置模板目录
        cfg.setDefaultEncoding("UTF-8");

        // 加载模板
        Template template = cfg.getTemplate(templateName);

        // 创建目标文件
        File outputFile = new File(outputFileNamePath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs(); // 确保目标目录存在
        }

        // 将模板内容写入文件
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
            template.process(dataModel, writer);
        }
        System.out.println("文件生成成功: " + outputFileNamePath);

    }
}
