package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import classes.GameClass;
import main.FindBestCombination;

import java.util.ArrayList;
import java.util.List;

public class BoltzmannSelector implements ParentSelector {

    //Constants to calculate the temperature, passed in the configuration file
    private final double t_0;
    private final double t_c;
    private final double k;

    public BoltzmannSelector(double t_0, double t_c, double k) {
        this.t_0 = t_0;
        this.t_c = t_c;
        this.k = k;
    }

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<Double> pseudoFitnesses = new ArrayList<>();
        double boltzmannAverage = getBoltzmannAverage(population);
        for (GameClass individual : population) {
            pseudoFitnesses.add(getBoltzmannPseudoFitness(individual, boltzmannAverage));
        }

        List<Double> relativeFitnesses = ParentSelectorManager.calculateRelativeFitnesses(pseudoFitnesses);
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnesses(relativeFitnesses);
        List<Double> randomNumbers = ParentSelectorManager.generateRandomNumbers(parentsAmount);

        System.out.println("Original fitnesses: ");
        for (GameClass individual : population) {
            System.out.println(individual.getBestPerformance());
        }

        System.out.println("Pseudo fitnesses: ");
        for (Double pseudoFit : pseudoFitnesses) {
            System.out.println(pseudoFit);
        }

        System.out.println("Relative fitnesses: ");
        for (Double relFit : relativeFitnesses) {
            System.out.println(relFit);
        }

        System.out.println("Cummulative fitnesses: ");
        for (Double acummFit : accumulatedRelativeFitnesses) {
            System.out.println(acummFit);
        }

        System.out.println("Random numbers: ");
        for (Double randNum : randomNumbers) {
            System.out.println(randNum);
        }

        return ParentSelectorManager.rouletteWheelSelect(population, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }

    private double getBoltzmannPseudoFitness(GameClass individual, double boltzmannAverage) {
        return Math.exp(individual.getBestPerformance()/getTemperature())/boltzmannAverage;
    }

    private double getTemperature() {
        return t_c + (t_0 - t_c)*Math.exp(-k* FindBestCombination.getCurrentGeneration());
    }

    private double getBoltzmannAverage(List<GameClass> population) {
        double boltzmannAverage = 0.0;
        for (GameClass individual : population) {
            boltzmannAverage += Math.exp(individual.getBestPerformance()/getTemperature());
        }

        return boltzmannAverage/population.size();
    }
}
