package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface ParentSelector {

    List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount);
}
