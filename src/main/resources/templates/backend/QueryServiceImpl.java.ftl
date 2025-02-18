package ${packageName};
import ${queryServicePackageName};
import com.fasterxml.jackson.databind.JsonNode;
import org.mysim.core.log.ActionLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mysim.core.utils.ActionLogUtils;
import org.mysim.core.utils.JsonUtils;
import org.mysim.core.utils.StatusUtils;
import java.util.Map;
import org.springframework.stereotype.Service;
@Service
<#noparse>
public class QueryServiceImpl implements QueryService{

    @Override
    public Page<ActionLog> getActionLogBySimulatorId(String simulatorId, int pageNum, int pageSize) {
        return ActionLogUtils.getActionLogBySimulatorId(simulatorId, pageNum, pageSize);
    }
    @Override
    public Map<String, Object> getPropertyBySimulatorId(String simulatorId) {
        return JsonUtils.jsonToMap(StatusUtils.getPropertyBySimulatorId(simulatorId));
    }
    @Override
    public Map<String, Integer> getSimulatorMap() {
        return StatusUtils.getTypeStatics();
    }
}
</#noparse>