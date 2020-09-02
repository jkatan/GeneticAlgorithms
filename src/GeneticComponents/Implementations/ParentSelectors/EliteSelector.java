package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import classes.GameClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EliteSelector implements ParentSelector {

    private final Comparator<GameClass> performanceComparator;

    public EliteSelector() {
        performanceComparator = (o1, o2) -> (int) (o1.getBestPerformance() - o2.getBestPerformance());
    }

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        population.sort(performanceComparator);
        int currentAmount = 0;
        int currentIndex = 0;
        List<GameClass> newParents = new ArrayList<>();
        for (final GameClass currentIndividual : population) {
            int timesToSelectIndividual = (int) Math.ceil((double) (parentsAmount - currentIndex) / (double) population.size());
            for (int i=0; i<timesToSelectIndividual; i++) {
                newParents.add(currentIndividual);
                currentAmount++;
            }

            if (currentAmount >= parentsAmount) {
                return newParents;
            }

            currentIndex += 1;
        }

        return newParents;
    }
}
