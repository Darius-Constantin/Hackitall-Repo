package com.ctrlaltdefeat.notifications;

import com.intellij.openapi.components.Service;

@Service
public final class CyclePhase {
    public enum Phase {
        WORK,
        BREAK
    }

    private Phase currentPhase = Phase.WORK;

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase phase) {
        this.currentPhase = phase;
    }

    public boolean isWorkPhase() {
        return currentPhase == Phase.WORK;
    }
}
