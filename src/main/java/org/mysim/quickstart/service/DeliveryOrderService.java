package org.mysim.quickstart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mysim.quickstart.pojo.DeliveryOrder;
import org.springframework.stereotype.Service;

public interface DeliveryOrderService extends IService<DeliveryOrder> {
    long getSuccessNum();
}
