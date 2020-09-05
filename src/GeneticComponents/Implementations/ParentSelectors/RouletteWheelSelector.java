package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class RouletteWheelSelector implements ParentSelector {
    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnessesFromPopulation(population);
        List<Double> randomNumbers = ParentSelectorManager.generateRandomNumbers(parentsAmount);
        return ParentSelectorManager.roulletteWheelSelect(population, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }
}
