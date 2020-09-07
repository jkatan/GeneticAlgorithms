package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;
import classes.GameClass;

import java.util.List;

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
    public void update(List<GameClass> population) {
        currentGeneration += 1;
    }

    @Override
    public boolean requiresFitnessToUpdate() {
        return false;
    }

    @Override
    public boolean isConditionMet() {
        return currentGeneration == maxGenerations;
    }
}
