package org.mysim.quickstart.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryOrder {
    @TableId
    private int id;
    private LocalDateTime generatedTime;
}
