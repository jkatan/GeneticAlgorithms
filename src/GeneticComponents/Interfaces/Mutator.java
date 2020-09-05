package GeneticComponents.Interfaces;

import GeneticComponents.Implementations.Mutators.MutatorManager;
import classes.GameClass;

import java.io.IOException;
import java.util.List;

public interface Mutator {

    /* mutate a given population in place */
    void mutate(GameClass individualToMutate, MutatorManager mutatorManager) throws IOException;
}
