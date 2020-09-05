package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProbabilisticTournamentSelector implements ParentSelector {

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<GameClass> parentsToSelect = new ArrayList<>();
        int[] randomIndexes;
        double randomThreshold;
        GameClass randomIndividual1;
        GameClass randomIndividual2;
        double randomNumber;

        Random randomGenerator = new Random();

        for (int i=0; i<parentsAmount; i++) {
            randomThreshold = (randomGenerator.nextDouble()+1.0)/2.0;
            randomIndexes = randomGenerator.ints(0, population.size()).distinct().limit(2).toArray();
            randomIndividual1 = population.get(randomIndexes[0]);
            randomIndividual2 = population.get(randomIndexes[1]);
            randomNumber = randomGenerator.nextDouble();

            if (randomNumber < randomThreshold) {
                parentsToSelect.add(getBestIndividual(randomIndividual1, randomIndividual2));
            } else {
                parentsToSelect.add(getWorstIndividual(randomIndividual1, randomIndividual2));
            }
        }

        return parentsToSelect;
    }

    private GameClass getBestIndividual(GameClass individual1, GameClass individual2) {
        return individual1.getBestPerformance() > individual2.getBestPerformance() ? individual1 : individual2;
    }

    private GameClass getWorstIndividual(GameClass individual1, GameClass individual2) {
        return individual1.getBestPerformance() < individual2.getBestPerformance() ? individual1 : individual2;
    }
}
