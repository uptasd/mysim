package org.mysim.core.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.mysim.core.log.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ActionLogUtils {
    private static IService<ActionLog> actionLogService;
    @Autowired
    public ActionLogUtils(IService<ActionLog> service){
        ActionLogUtils.actionLogService = service;
    }

    public void setActionLogService(IService<ActionLog> service) {
        actionLogService = service;
    }

    public static void save(ActionLog log) {
        actionLogService.save(log);
    }
    public static void saveOrUpdate(ActionLog log){
        actionLogService.saveOrUpdate(log);
    }

    public static ActionLog getActionLogById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return actionLogService.getById(id);
    }
    public static List<ActionLog> getAllActionLogBySimulatorId(String simulatorId){
        QueryWrapper<ActionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actor_id", simulatorId); // 假设数据库字段名为 actor_id，需与实际数据库字段名保持一致
        queryWrapper.orderByAsc("turn"); // 根据 turn 字段升序排序

        // 查询符合条件的记录并返回
        return actionLogService.list(queryWrapper);
    }
    public static Page<ActionLog> getActionLogBySimulatorId(String simulatorId, int pageNum, int pageSize) {
        // 创建分页对象
        Page<ActionLog> page = new Page<>(pageNum, pageSize);

        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<ActionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actor_id", simulatorId); // 假设数据库字段名为 actor_id
        queryWrapper.orderByAsc("turn"); // 根据 turn 字段升序排序

        // 执行分页查询
        return actionLogService.page(page, queryWrapper);
    }
}
