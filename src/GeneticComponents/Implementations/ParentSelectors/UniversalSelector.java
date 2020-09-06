package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class UniversalSelector implements ParentSelector {

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnessesFromPopulation(population);
        List<Double> randomNumbers = generateRandomNumbers(parentsAmount);
        return ParentSelectorManager.rouletteWheelSelect(population, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }

    private List<Double> generateRandomNumbers(int numbersAmount) {
        double randomNumber = Utils.getRandomInRange(0.0, 1.0);
        List<Double> randomNumbers = new ArrayList<>();
        for (int i=0; i<numbersAmount; i++) {
            randomNumbers.add((randomNumber+(double)(i))/(double)(numbersAmount));
        }

        return randomNumbers;
    }
}
