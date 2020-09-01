package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface Mutator {

    /* mutate a given population in place */
    void mutate(List<GameClass> populationToMutate);
}
