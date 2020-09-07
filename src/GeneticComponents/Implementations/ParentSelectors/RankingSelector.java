package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class RankingSelector implements ParentSelector {

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<GameClass> rankedPopulation = new ArrayList<>(population);

        // First we order the population based on individual's performance in descending order
        rankedPopulation.sort((o1, o2) -> Double.compare(o2.getBestPerformance(), o1.getBestPerformance()));

        // Now we calculate the pseudo fitness function based on this ranking
        double rankedPopulationSize = rankedPopulation.size();
        List<Double> absoluteFitnesses = new ArrayList<>();
        for (int i=0; i<rankedPopulationSize; i++) {
            absoluteFitnesses.add((rankedPopulationSize-i)/ rankedPopulationSize);
        }

        List<Double> relativeFitnesses = ParentSelectorManager.calculateRelativeFitnesses(absoluteFitnesses);
        List<Double> accumulatedRelativeFitnesses = ParentSelectorManager.calculateAccumulatedRelativeFitnesses(relativeFitnesses);
        List<Double> randomNumbers = ParentSelectorManager.generateRandomNumbers(parentsAmount);

        return ParentSelectorManager.rouletteWheelSelect(rankedPopulation, parentsAmount,
                accumulatedRelativeFitnesses, randomNumbers);
    }
}
