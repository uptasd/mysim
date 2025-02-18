package org.mysim.quickstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mysim.quickstart.mapper.DeliveryOrderMapper;
import org.mysim.quickstart.pojo.DeliveryOrder;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryOrderServiceImpl extends ServiceImpl<DeliveryOrderMapper,DeliveryOrder> implements DeliveryOrderService{

    @Override
    public long getSuccessNum() {
        QueryWrapper<DeliveryOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "done");

        // 使用 count 方法获取符合条件的记录数量
        return this.count(queryWrapper);
    }
}
