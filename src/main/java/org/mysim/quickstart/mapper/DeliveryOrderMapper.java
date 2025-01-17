package org.mysim.quickstart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mysim.quickstart.pojo.DeliveryOrder;

@Mapper
public interface DeliveryOrderMapper extends BaseMapper<DeliveryOrder> {
}
