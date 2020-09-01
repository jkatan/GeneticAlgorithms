package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface PopulationGenerator {

    List<GameClass> generateFromCurrentPopulation(List<GameClass> parents, List<GameClass> children);
}
