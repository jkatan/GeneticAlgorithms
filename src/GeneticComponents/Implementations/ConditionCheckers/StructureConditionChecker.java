package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;
import classes.GameClass;

import java.util.LinkedList;
import java.util.List;

public class StructureConditionChecker implements ConditionChecker {
    private final int maxGenerationsWithoutChanges;
    private final double significantPercentage;
    private int amountOfGenerationsWithoutChanges;
    private List<GameClass> lastPopulation;

    public StructureConditionChecker(int nGenerations, double significantPercentage) {
        this.maxGenerationsWithoutChanges = nGenerations;
        this.significantPercentage = significantPercentage;
    }

    public StructureConditionChecker(int nGenerations) {
        this(nGenerations, 55);
    }

    @Override
    public void initialize() {
        amountOfGenerationsWithoutChanges = 0;
    }

    @Override
    public void update(List<GameClass> population) {
        int repeatedIndividuals = 0;
        if (lastPopulation != null) {
            for (GameClass i: population) {
                if (lastPopulation.contains(i)) {
                    repeatedIndividuals++;
                }
            }
            amountOfGenerationsWithoutChanges++;
            if ((repeatedIndividuals / (double) population.size()) < significantPercentage) {
                amountOfGenerationsWithoutChanges = 0;
            }
        }
        lastPopulation = new LinkedList<>(population);
    }

    @Override
    public boolean requiresFitnessToUpdate() {
        return true;
    }

    @Override
    public boolean isConditionMet() {
        return amountOfGenerationsWithoutChanges >= maxGenerationsWithoutChanges;
    }
}
