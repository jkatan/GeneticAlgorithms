package GeneticComponents.Implementations.PopulationGenerators;

import GeneticComponents.Interfaces.PopulationGenerator;
import Utils.Utils;
import classes.GameClass;

import java.util.*;

/*
* Pick random individuals from the set { current population + children }
* */
public class FillAllGenerator implements PopulationGenerator {
    @Override
    public List<GameClass> generateFromCurrentPopulation(List<GameClass> population, List<GameClass> children, int amountToGenerate) {
        List<GameClass> populationToReturn = new ArrayList<>();
        List<GameClass> populationCopy = new ArrayList<>(population);
        populationCopy.addAll(children);
        Collections.shuffle(populationCopy);

        PopulationGeneratorManager.addIndividualsToPopulation(populationCopy, populationToReturn, amountToGenerate);

        return populationToReturn;
    }
}
