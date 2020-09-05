package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;

public class AcceptableSolutionConditionChecker implements ConditionChecker {
    private final double fitness;
    private double maxFitnessInGeneration;

    public AcceptableSolutionConditionChecker(double fitness) {
        this.fitness = fitness;
        this.maxFitnessInGeneration = 0;
    }

    @Override
    public void initialize() {
        // N/A
    }

    @Override
    public void update(Double newValue) {
        maxFitnessInGeneration = newValue;
    }

    @Override
    public boolean isConditionMet() {
        return maxFitnessInGeneration >= fitness;
    }
}
