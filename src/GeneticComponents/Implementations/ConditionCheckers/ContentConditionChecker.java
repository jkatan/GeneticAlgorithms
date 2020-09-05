package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;

public class ContentConditionChecker implements ConditionChecker {
    private final int maxGenerationsWithoutChanges;
    private double fitness;
    private int amountOfGenerationsWithoutChanges;

    public ContentConditionChecker(int nGenerations) {
        this.maxGenerationsWithoutChanges = nGenerations;
        this.fitness = 0;
        this.amountOfGenerationsWithoutChanges = 0;
    }

    @Override
    public void initialize() {
        amountOfGenerationsWithoutChanges = 0;
        fitness = 0;
    }

    @Override
    public void update(Double newValue) {
        double thisGenFitness = newValue;
        if (thisGenFitness == fitness) {
            amountOfGenerationsWithoutChanges++;
        } else {
            amountOfGenerationsWithoutChanges = 0;
            fitness = thisGenFitness;
        }
    }

    @Override
    public boolean isConditionMet() {
        return amountOfGenerationsWithoutChanges >= maxGenerationsWithoutChanges;
    }
}
