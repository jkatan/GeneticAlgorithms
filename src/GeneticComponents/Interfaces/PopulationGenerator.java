package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface PopulationGenerator {

    List<GameClass> generateFromCurrentPopulation(List<GameClass> population, List<GameClass> children, int amountToGenerate);
}
