package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class UniversalSelector implements ParentSelector {

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnesses(population);
        List<Double> randomNumbers = generateRandomNumbers(parentsAmount);
        return ParentSelectorManager.roulletteWheelSelect(population, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }

    private List<Double> generateRandomNumbers(int numbersAmount) {
        double randomNumber = Utils.getRandomInRange(0.0, 1.0);
        System.out.println("Random number: ");
        System.out.println(randomNumber);
        List<Double> randomNumbers = new ArrayList<>();
        for (int i=0; i<numbersAmount; i++) {
            randomNumbers.add((randomNumber+(double)(i))/(double)(numbersAmount));
        }

        System.out.println("Random numbers generated: ");
        for (Double randNum : randomNumbers) {
            System.out.println(randNum);
        }

        return randomNumbers;
    }
}
