package org.mysim.core.simulator.ai;

import org.mysim.core.events.SystemEvents;
import org.mysim.core.events.action.system.SearchByType;
import org.mysim.core.simulator.SearchSimulator;
import org.mysim.core.simulator.Simulator;
import org.mysim.core.simulator.SimulatorAgent;

public class SearchSimulatorAI extends BaseAI {


    public SearchSimulatorAI(SearchSimulator searchSimulator) {
        SimulatorAgent agent = searchSimulator.getAgent();
        agent.registerSystemEventActor(SystemEvents.SEARCH_BY_TYPE.name(), new SearchByType(this));
    }

}
