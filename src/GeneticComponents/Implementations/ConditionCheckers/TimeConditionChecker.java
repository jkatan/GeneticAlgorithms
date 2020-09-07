package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;
import classes.GameClass;

import java.util.List;

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
    public void update(List<GameClass> population) {
        // N/A with time
    }

    @Override
    public boolean requiresFitnessToUpdate() {
        return false;
    }

    @Override
    public boolean isConditionMet() {
        return (System.currentTimeMillis() - startTime) / 1000 >= timeLimit;
    }
}
