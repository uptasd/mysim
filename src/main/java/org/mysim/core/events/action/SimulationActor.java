package org.mysim.core.events.action;

import lombok.Setter;
import org.mysim.core.message.SimMessage;
import org.mysim.core.simulator.ai.SimulatorAI;
import org.mysim.core.simulator.status.SimulatorProperty;
import org.mysim.core.simulator.status.SimulatorStatus;
import org.mysim.core.utils.JsonUtils;

import java.lang.reflect.Field;
import java.util.*;

public abstract class SimulationActor {
    @Setter
    public SimulatorAI simulatorAI;
    public ActionContext context;
    @Setter
    public String actionName;


    public SimulationActor(SimulatorAI simulatorAI) {
        this.simulatorAI = simulatorAI;
        context = new ActionContext();
    }


    public abstract void action(SimMessage simMessage);

    // user complete
    public abstract void execute(ActionContext context);

    public void buildContext(SimMessage simMessage) {
        context.setContext(ActionContext.SIMULATOR_PROPERTY, simulatorAI.getSimulatorProperty());
        context.setContext(ActionContext.SIMULATOR_STATUS, simulatorAI.getSimulatorStatus());
        context.setContext(ActionContext.MESSAGE_CONTEXT, simMessage);
    }

    public SimMessage getSimMessage() {
        return (SimMessage) context.getContext(ActionContext.MESSAGE_CONTEXT);
    }

    public SimulatorProperty getSimulatorProperty() {
        //update context
        context.setContext(ActionContext.SIMULATOR_PROPERTY, simulatorAI.getSimulatorProperty());
        return simulatorAI.getSimulatorProperty();
    }

    public SimulatorStatus getSimulatorStatus() {
        //update context
        context.setContext(ActionContext.SIMULATOR_STATUS, simulatorAI.getSimulatorStatus());
        return simulatorAI.getSimulatorStatus();
    }

    public void putContext(String key, Object obj) {
        context.setContext(key, obj);
    }

    public void putContext(Map<String, Object> ctx) {
        context.setContext(ctx);
    }

    public Map<String, Object> getContext(List<String> keys) {
        return context.getContext(keys);
    }

    public Object getContext(String key) {
        return context.getContext(key);
    }

    public <T> T getPayLoad(Class<T> PayLoadClass) {
        SimMessage eventMessage = getSimMessage();
        String payLoad = eventMessage.getPayLoad();
        return JsonUtils.jsonToObject(payLoad, PayLoadClass);
    }

    public String statusUpdateDesc(SimulatorStatus before, SimulatorStatus after) {
        if (before == null || after == null) {
            return "传入的对象不能为null";
        }

        // 获取 before 和 after 的状态集合
        Set<String> beforeStatus = new HashSet<>(before.getAllStatus());
        Set<String> afterStatus = new HashSet<>(after.getAllStatus());

        // 找到新增的状态
        Set<String> addedStatuses = new HashSet<>(afterStatus);
        addedStatuses.removeAll(beforeStatus);

        // 找到消失的状态
        Set<String> removedStatuses = new HashSet<>(beforeStatus);
        removedStatuses.removeAll(afterStatus);

        // 构造结果字符串
        StringBuilder result = new StringBuilder();
        if (!addedStatuses.isEmpty()) {
            result.append("新增状态: ").append(String.join(", ", addedStatuses)).append("; ");
        }
        if (!removedStatuses.isEmpty()) {
            result.append("消失状态: ").append(String.join(", ", removedStatuses)).append("; ");
        }

        return !result.isEmpty() ? result.toString() : "";
    }

    public String propertyUpdateDesc(SimulatorProperty before, SimulatorProperty after) {
        if (before == null || after == null) {
            return "传入的对象不能为null";
        }

        if (!before.getClass().equals(after.getClass())) {
            return "两个对象的类型不匹配";
        }
        StringBuilder changes = new StringBuilder();
        Class<?> clazz = before.getClass();
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            try {
                field.setAccessible(true); // 设置可访问性，获取 private 字段

                Object beforeValue = field.get(before); // 获取 before 对象的字段值
                Object afterValue = field.get(after);   // 获取 after 对象的字段值

                // 判断字段是否发生变化
                if (beforeValue == null && afterValue == null) {
                    continue; // 如果两个值都为 null，跳过
                }

                if (beforeValue == null || afterValue == null || !beforeValue.equals(afterValue)) {
                    // 如果字段值不相等，记录变化
                    changes.append(field.getName())
                            .append(": ")
                            .append(beforeValue)
                            .append(" -> ")
                            .append(afterValue)
                            .append("; ");
                }

            } catch (IllegalAccessException e) {
                System.err.println("无法访问字段: " + field.getName());
            }
        }

        return !changes.isEmpty() ? changes.toString() : "";
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields(); // 获取当前类的字段，包括 private
            fields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass(); // 向父类递归
        }
        return fields;
    }
}
