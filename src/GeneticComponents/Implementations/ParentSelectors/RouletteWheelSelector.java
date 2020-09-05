package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class RouletteWheelSelector implements ParentSelector {
    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnesses(population);
        List<Double> randomNumbers = generateRandomNumbers(parentsAmount);
        return ParentSelectorManager.roulletteWheelSelect(population, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }

    private List<Double> generateRandomNumbers(int numbersAmount) {
        List<Double> randomNumbers = new ArrayList<>();
        for (int i=0; i<numbersAmount; i++) {
            randomNumbers.add(Utils.getRandomInRange(0.0, 1.0));
        }

        return randomNumbers;
    }
}
