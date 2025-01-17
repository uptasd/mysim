package org.mysim.core.simulator.status;

import java.util.*;

public class SimulatorStatus {
    private final Set<String> activeStatus;

    public SimulatorStatus() {
        activeStatus = new HashSet<>();
    }

   public Set<String> getAllStatus() {
        return activeStatus;
    }

    void addStatus(String status) {
        activeStatus.add(status);
    }

    void removeStatus(String status) {
        activeStatus.remove(status);
    }

    boolean containsStatus(String statusName) {
        return activeStatus.contains(statusName);
    }

    public void updateStatus(SimulatorProperty property) {
        StatusManager.checkAndUpdateStates(property, this);
    }

}
