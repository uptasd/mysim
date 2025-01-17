package org.mysim.core.log;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog {
    @TableId
    private long action_id;
    private Long turn;
    private String actorId;
    private String actionName;
    private String propertyDesc;
    private String statusDesc;
}
