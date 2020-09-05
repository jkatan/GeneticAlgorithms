package GeneticComponents.Implementations.ConditionCheckers;

import GeneticComponents.Interfaces.ConditionChecker;

public class StructureConditionChecker implements ConditionChecker {
    private final int maxGenerationsWithoutChanges;
    private final double significantPercentage;
    private int amountOfGenerationsWithoutChanges;

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

    /**
     * Amount of population that changed.
     * @param newValue The percentage of population that changed from last generation.
     */
    @Override
    public void update(Double newValue) {
        amountOfGenerationsWithoutChanges++;
        if (newValue < significantPercentage) {
            amountOfGenerationsWithoutChanges = 0;
        }
    }

    @Override
    public boolean isConditionMet() {
        return amountOfGenerationsWithoutChanges >= maxGenerationsWithoutChanges;
    }
}
