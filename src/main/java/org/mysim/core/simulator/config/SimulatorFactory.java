package org.mysim.core.simulator.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.mysim.core.simulator.ResourceSimulator;
import org.mysim.core.simulator.SearchSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.ai.ResourceAI;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.utils.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimulatorFactory {
    static HashMap<String, Class<? extends SimulatorAI>> simulatorAIClassMap = new HashMap<>();
    static HashMap<String, Class<? extends Simulator>> simulatorClassMap = new HashMap<>();
    static HashMap<String, Class<? extends SimulatorProperty>> propertyClassMap = new HashMap<>();

    static {
        List<SimulatorMetaInfo> metaInfo = SimulatorMetaInfoLoader.getMetaInfo();
        for (SimulatorMetaInfo info : metaInfo) {
            String simulatorType = info.getSimulatorType();
            Class<? extends SimulatorAI> simulatorAIClass = SimulatorAI.class;
            Class<? extends Simulator> simulatorClass = Simulator.class;
            Class<? extends SimulatorProperty> propertyClass = SimulatorProperty.class;

            try {
                if (info.getSimulatorAIClass() != null) {
                    simulatorAIClass = (Class<? extends SimulatorAI>) Class.forName(info.getSimulatorAIClass());

                }
                if (info.getSimulatorClass() != null) {
                    simulatorClass = (Class<? extends Simulator>) Class.forName(info.getSimulatorClass());
                }
                if (info.getPropertyClass() != null) {
                    propertyClass = (Class<? extends SimulatorProperty>) Class.forName(info.getPropertyClass());
                }
                simulatorAIClassMap.put(simulatorType, simulatorAIClass);
                simulatorClassMap.put(simulatorType, simulatorClass);
                propertyClassMap.put(simulatorType, propertyClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static List<Simulator> loadAllRes(String folderPath) {
        List<Simulator> ret = new ArrayList<>();
        try {
            List<String> filenames = getAllJsonFileName(folderPath);
            if (filenames == null) {
                return ret;
            }
            for (String fileName : filenames) {
                List<Simulator> simulators = loadSimulators(folderPath + "/" + fileName);
                ret.addAll(simulators);
            }
            return ret;
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getAllJsonFileName(String folderPath) throws URISyntaxException, IOException {
        List<String> fileNames = new ArrayList<>();
        URL resourceUrl = SimulatorFactory.class.getClassLoader().getResource(folderPath);
        if (resourceUrl == null) {
            return null;
        }
        Path folder = Paths.get(resourceUrl.toURI());
        // 遍历文件夹，过滤出 .json 文件并获取文件名
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.json")) {
            for (Path path : stream) {
                fileNames.add(path.getFileName().toString());  // 只获取文件名
            }
        }
        return fileNames;
    }

    public static List<Simulator> loadSimulators(String resourceFilePath) {
        List<Object> properties = JsonUtils.resToObj(resourceFilePath, new TypeReference<>() {
        });
        List<Simulator> simulators = new ArrayList<>();
        for (Object simulatorProperty : properties) {
            String simulatorType = ((Map<String, String>) simulatorProperty).getOrDefault("simulatorType", "");
            Class<? extends SimulatorProperty> propertyClass = propertyClassMap.getOrDefault(simulatorType, SimulatorProperty.class);
            Class<? extends Simulator> simulatorClass = simulatorClassMap.getOrDefault(simulatorType, ResourceSimulator.class);
            Class<? extends SimulatorAI> AIClass = simulatorAIClassMap.getOrDefault(simulatorType, ResourceAI.class);
            SimulatorProperty property = JsonUtils.mapToObj(simulatorProperty, propertyClass);
            try {
                Simulator simulator = buildSimulator(property, simulatorClass, AIClass);
                simulators.add(simulator);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        return simulators;
    }

    static Simulator buildSimulator(SimulatorProperty property, Class<? extends Simulator> simulatorClass, Class<? extends SimulatorAI> simulatorAIClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<? extends SimulatorAI> aiConstructor = simulatorAIClass.getDeclaredConstructor();
        SimulatorAI ai = aiConstructor.newInstance();
        Constructor<? extends Simulator> constructor = simulatorClass.getDeclaredConstructor(SimulatorProperty.class, SimulatorAI.class);
        return constructor.newInstance(property, ai);
    }
}
