package org.mysim.quickstart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mysim.quickstart.mapper.DeliveryOrderMapper;
import org.mysim.quickstart.pojo.DeliveryOrder;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryOrderServiceImpl extends ServiceImpl<DeliveryOrderMapper,DeliveryOrder> implements DeliveryOrderService{

}
