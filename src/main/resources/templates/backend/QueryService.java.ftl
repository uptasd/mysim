package ${packageName};
import java.util.List;
import java.util.Map;
import org.mysim.core.log.ActionLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
<#noparse>
public interface QueryService {
    Page<ActionLog> getActionLogBySimulatorId(String simulatorId,int pageNum,int pageSize);
    Map<String, Object> getPropertyBySimulatorId(String simulatorId);
    Map<String,Integer> getSimulatorMap();
}
</#noparse>