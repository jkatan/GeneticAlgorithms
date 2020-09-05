package GeneticComponents.Implementations.ParentSelectors;

import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class ParentSelectorManager {

    public static List<GameClass> roulletteWheelSelect(List<GameClass> population, int parentsAmount,
          List<Double> accumulatedRelativeFitnesses, List<Double> randomNumbers) {

        List<GameClass> parentsSelection = new ArrayList<>(parentsAmount);

        System.out.println("Accumulated relative fitnesses: ");
        for (Double accumulatedRelFitn : accumulatedRelativeFitnesses) {
            System.out.println(accumulatedRelFitn);
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

    public static List<Double> calculateAccumulatedRelativeFitnesses(List<GameClass> population) {
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

    private static List<Double> calculateRelativeFitnesses(List<GameClass> population) {
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
