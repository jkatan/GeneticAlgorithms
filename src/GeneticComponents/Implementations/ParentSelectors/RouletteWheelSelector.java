package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class RouletteWheelSelector implements ParentSelector {
    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> accumulatedRelativeFitnesses = calculateAccumulatedRelativeFitnesses(population);
        List<Double> randomNumbers = generateRandomNumbers(parentsAmount);

        List<GameClass> parentsSelection = new ArrayList<>(parentsAmount);

        for (Double accumRelFitness : accumulatedRelativeFitnesses) {
            System.out.println(accumRelFitness);
        }

        for (Double randNum : randomNumbers) {
            System.out.println(randNum);
        }

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

    private List<Double> generateRandomNumbers(int numbersAmount) {
        List<Double> randomNumbers = new ArrayList<>();
        for (int i=0; i<numbersAmount; i++) {
            randomNumbers.add(Utils.getRandomInRange(0.0, 1.0));
        }

        return randomNumbers;
    }

    private List<Double> calculateAccumulatedRelativeFitnesses(List<GameClass> population) {
        List<Double> relativeFitness = calculateRelativeFitnesses(population);
        for (Double relFitness : relativeFitness) {
            System.out.println(relFitness);
        }

        List<Double> accumulatedRelativeFitness = new ArrayList<>();
        accumulatedRelativeFitness.add(relativeFitness.get(0));

        for (int i=1; i<relativeFitness.size(); i++) {
            accumulatedRelativeFitness.add(relativeFitness.get(i) + accumulatedRelativeFitness.get(i-1));
        }

        return accumulatedRelativeFitness;
    }

    private List<Double> calculateRelativeFitnesses(List<GameClass> population) {
        List<Double> relativeFitnessOfIndividuals = new ArrayList<>();
        double totalFitness = 0.0;

        for (final GameClass individual : population) {
            totalFitness += individual.getBestPerformance();
        }

        for (final GameClass individual : population) {
            relativeFitnessOfIndividuals.add(individual.getBestPerformance()/totalFitness);
        }

        return relativeFitnessOfIndividuals;
    }
}
