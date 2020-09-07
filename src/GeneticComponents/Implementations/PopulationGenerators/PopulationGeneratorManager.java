package GeneticComponents.Implementations.PopulationGenerators;

import classes.GameClass;

import java.util.List;

public class PopulationGeneratorManager {

    public static void addIndividualsToPopulation(List<GameClass> individualsToAdd, List<GameClass> population, int amountToAdd) {
        for (int i=0; i<amountToAdd; i++) {
            population.add(individualsToAdd.get(i));
        }
    }
}
