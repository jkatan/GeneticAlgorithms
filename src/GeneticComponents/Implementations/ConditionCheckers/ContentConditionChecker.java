package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;
import classes.GameClass;

import java.util.List;

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
    public void update(List<GameClass> population) {
        double thisGenFitness = getBestFitness(population);
        if (thisGenFitness == this.fitness) {
            amountOfGenerationsWithoutChanges++;
        } else {
            amountOfGenerationsWithoutChanges = 0;
            this.fitness = thisGenFitness;
        }
    }

    @Override
    public boolean requiresFitnessToUpdate() {
        return true;
    }

    @Override
    public boolean isConditionMet() {
        return amountOfGenerationsWithoutChanges >= maxGenerationsWithoutChanges;
    }

    private double getBestFitness(List<GameClass> population) {
        double bestFitness = 0;
        for (GameClass i: population) {
            double individualFitness = i.getBestPerformance();
            if (individualFitness > bestFitness) {
                bestFitness = individualFitness;
            }
        }
        return bestFitness;
    }
}
