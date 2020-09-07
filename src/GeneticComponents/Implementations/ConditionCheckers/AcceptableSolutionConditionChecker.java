package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;
import classes.GameClass;

import java.util.List;

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
    public void update(List<GameClass> population) {
        maxFitnessInGeneration = getBestFitness(population);
    }

    @Override
    public boolean requiresFitnessToUpdate() {
        return true;
    }

    @Override
    public boolean isConditionMet() {
        return maxFitnessInGeneration >= fitness;
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
