package ${packageName};
import ${QueryServicePackageName}.QueryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.mysim.core.log.ActionLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
<#noparse>
public class QueryController {
    QueryService queryService;
    @GetMapping("/api/simulator/actionlog")
    public Page<ActionLog> getActionLogBySimulatorId(@RequestParam String simulatorId,
                                                    @RequestParam int pageNum,
                                                    @RequestParam int pageSize) {
        return queryService.getActionLogBySimulatorId(simulatorId,pageNum,pageSize);
    }
    @GetMapping("/api/simulator/status")
    public Map<String, Object> getPropertyBySimulatorId(@RequestParam String simulatorId){
        return queryService.getPropertyBySimulatorId(simulatorId);
    }
    @GetMapping("/api/simulator/statics")
    public Map<String,Integer> getSimulatorMap(){
        return queryService.getSimulatorMap();
    }
}
</#noparse>