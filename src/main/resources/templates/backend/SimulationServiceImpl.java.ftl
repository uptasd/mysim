package ${packageName};
import ${SimulationServicePackageName};
import org.springframework.stereotype.Service;
import org.mysim.core.rt.container.MyContainer;
import org.mysim.core.rt.container.SimulationContainer;

@Service
public class SimulationServiceImpl implements SimulationService{
    SimulationContainer container;
    @Override
    public void init(){
        container = new MyContainer();
        container.loadSimulatorFromDir("simulation/init-properties");
    }
    @Override
    public void step(long timeInterval){
        if (container == null) {
            init();
        }
        container.step(timeInterval);
    }
}