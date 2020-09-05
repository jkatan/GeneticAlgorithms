package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;

public class GenerationConditionChecker implements ConditionChecker {
    private final int maxGenerations;
    private double currentGeneration;

    public GenerationConditionChecker(int nGenerations) {
        this.maxGenerations = nGenerations;
    }

    @Override
    public void initialize() {
        currentGeneration = 0;
    }

    @Override
    public void update(Double newValue) {
        currentGeneration += 1;
    }

    @Override
    public boolean isConditionMet() {
        return currentGeneration == maxGenerations;
    }
}
