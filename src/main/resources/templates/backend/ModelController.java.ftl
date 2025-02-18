package ${packageName};

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ${commonPackage}.*;
import ${SimulationServicePackage}.SimulationService;

@AllArgsConstructor
@RestController
public class ModelController{
    SimulationService simulationService;
    @GetMapping("/api/model/init")
    public Result modelInit() {
        simulationService.init();
        return Result.success();
    }
    @GetMapping("/api/model/nextRound")
    public Result modelStep(@RequestParam long blockTime) {
        simulationService.step(blockTime);
        return Result.success("ok");
    }
}