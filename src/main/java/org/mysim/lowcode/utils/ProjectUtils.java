package org.mysim.lowcode.utils;

import freemarker.template.TemplateException;
import org.mysim.lowcode.BuildStrategy;
import org.mysim.lowcode.config.ProjectMetaConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ProjectUtils {
    private static Process springBootProcess;

    public static void runProject(String projectDir) throws Exception {
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (springBootProcess != null && springBootProcess.isAlive()) {
                System.out.println("Shutting down Spring Boot process...");
                springBootProcess.destroyForcibly(); // 强制终止子进程
            }
        }));

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File(projectDir));
        builder.command("cmd.exe", "/c", "mvn clean package");
        Process process = builder.start();
        printOutput(process, Charset.forName("GBK"));
        int exitCode = process.waitFor();
        System.out.println("mvn clean package exited with code: " + exitCode);

        if (exitCode == 0) {
            // 如果 mvn clean package 成功，执行 java -jar 命令
            builder.command("cmd.exe", "/c", "java -jar .\\target\\quick-run.jar"); // Windows
            // builder.command("sh", "-c", "java -jar ./target/demo-0.0.1.jar"); // Linux/Mac
            springBootProcess = builder.start(); // 保存 Spring Boot 进程的引用
            printOutput(springBootProcess, Charset.forName("UTF-8"));
            exitCode = springBootProcess.waitFor();
            System.out.println("java -jar exited with code: " + exitCode);
        } else {
            System.err.println("mvn clean package failed, skipping java -jar");
        }
    }

    public static void buildSimulationProject(String artifactId, String outputDir) throws IOException, TemplateException {
        buildEmptyProject("org.mysim", artifactId, "0.0.1", outputDir);
        String root_path = outputDir + "/" + artifactId;
        FreeMakerUtils.buildPomFile(artifactId, root_path + "/pom.xml");
        FreeMakerUtils.buildApplicationFile(null, root_path + "/src/main/resources/application.yml");
        String srcDir = root_path + "/src/main/java/org/mysim/" + artifactId;
        String packageRoot = "org.mysim." + artifactId;
        BuildStrategy.build("ModelController", srcDir, packageRoot);
        BuildStrategy.build("Result",srcDir, packageRoot);
        BuildStrategy.build("SimulationService",srcDir,packageRoot);
        BuildStrategy.build("SimulationServiceImpl",srcDir,packageRoot);
        BuildStrategy.build("QueryController",srcDir,packageRoot);
        BuildStrategy.build("QueryService",srcDir,packageRoot);
        BuildStrategy.build("QueryServiceImpl",srcDir,packageRoot);
//        FreeMakerUtils.buildControllers(srcDir, packageRoot);
//        FreeMakerUtils.buildCommonFiles(srcDir, packageRoot);
    }

    public static void buildEmptyProject(String groupId, String artifactId, String version, String outputDir) throws IOException {
        String url = buildSpringBootInitializrURL(groupId, artifactId, version);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        // 获取响应流
        InputStream inputStream = connection.getInputStream();

        // 创建输出目录
        File outputFile = new File(outputDir + "/" + artifactId + ".zip");
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        }
        // 将响应保存为zip文件
        Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 解压生成的zip文件
        unzip(outputFile, new File(outputDir));
        // 关闭连接
        connection.disconnect();
    }

    public static String buildSpringBootInitializrURL(String groupId, String artifactId, String version) {
        return String.format(
                "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=%s&baseDir=%s&groupId=%s&artifactId=%s&version=%s&packageName=%s.%s&dependencies=web,data-jpa",
                ProjectMetaConfig.BOOT_VERSION, artifactId, groupId, artifactId, version, groupId, artifactId
        );
    }

    private static void unzip(File zipFile, File destDir) throws IOException {
        try (java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(new FileInputStream(zipFile))) {
            java.util.zip.ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                File filePath = new File(destDir, entry.getName());
                if (!entry.isDirectory()) {
                    new File(filePath.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] bytesIn = new byte[1024];
                        int read;
                        while ((read = zipIn.read(bytesIn)) != -1) {
                            bos.write(bytesIn, 0, read);
                        }
                    }
                } else {
                    filePath.mkdirs();
                }
                zipIn.closeEntry();
            }
        }
        zipFile.delete(); // 删除临时zip文件
    }

    private static void printOutput(Process process, Charset charset) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
