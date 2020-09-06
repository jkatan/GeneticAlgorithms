package GeneticComponents.Implementations.ParentSelectors;

import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class ParentSelectorManager {

    public static List<GameClass> rouletteWheelSelect(List<GameClass> population, int parentsAmount,
              List<Double> accumulatedRelativeFitnesses, List<Double> randomNumbers) {

        List<GameClass> parentsSelection = new ArrayList<>(parentsAmount);

        int currentIndividualIndex;
        boolean stopIteratingRelativeFitness;
        while (!randomNumbers.isEmpty()) {

            currentIndividualIndex = 0;
            stopIteratingRelativeFitness = false;

            while (currentIndividualIndex < accumulatedRelativeFitnesses.size() && !stopIteratingRelativeFitness) {
                if (randomNumbers.get(0) <= accumulatedRelativeFitnesses.get(currentIndividualIndex)) {
                    parentsSelection.add(population.get(currentIndividualIndex));
                    randomNumbers.remove(0);
                    stopIteratingRelativeFitness = true;
                }

                currentIndividualIndex++;
            }
        }

        return parentsSelection;
    }

    public static List<Double> calculateAccumulatedRelativeFitnessesFromPopulation(List<GameClass> population) {

        List<Double> absoluteFitnesses = new ArrayList<>();
        for (GameClass individual : population) {
            absoluteFitnesses.add(individual.getBestPerformance());
        }

        return calculateAccumulatedRelativeFitnesses(calculateRelativeFitnesses(absoluteFitnesses));
    }

    public static List<Double> calculateAccumulatedRelativeFitnesses(List<Double> relFitnesses) {

        List<Double> accumulatedRelativeFitness = new ArrayList<>();
        accumulatedRelativeFitness.add(relFitnesses.get(0));

        for (int i=1; i<relFitnesses.size(); i++) {
            accumulatedRelativeFitness.add(relFitnesses.get(i) + accumulatedRelativeFitness.get(i-1));
        }

        return accumulatedRelativeFitness;
    }

    public static List<Double> calculateRelativeFitnesses(List<Double> absoluteFitnesses) {
        List<Double> relativeFitnessOfIndividuals = new ArrayList<>();
        double totalFitness = 0.0;

        for (final Double absoluteFitness : absoluteFitnesses) {
            totalFitness += absoluteFitness;
        }

        for (final Double absoluteFitness : absoluteFitnesses) {
            relativeFitnessOfIndividuals.add(absoluteFitness/totalFitness);
        }

        return relativeFitnessOfIndividuals;
    }

    public static List<Double> generateRandomNumbers(int numbersAmount) {
        List<Double> randomNumbers = new ArrayList<>();
        for (int i=0; i<numbersAmount; i++) {
            randomNumbers.add(Utils.getRandomInRange(0.0, 1.0));
        }

        return randomNumbers;
    }
}
