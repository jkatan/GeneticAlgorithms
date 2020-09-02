package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;

public class TimeConditionChecker implements ConditionChecker {

    private final double timeLimit;
    private double startTime;

    public TimeConditionChecker(double time) {
        this.timeLimit = time;
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public Boolean isConditionMet() {
        return (System.currentTimeMillis() - startTime) / 1000 >= timeLimit;
    }
}
