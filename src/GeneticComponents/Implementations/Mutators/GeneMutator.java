package GeneticComponents.Implementations.Mutators;

import GeneticComponents.Interfaces.Mutator;
import Utils.Utils;
import classes.GameClass;

import java.io.IOException;

/*
* Gene mutator picks a random gene to mutate, and then mutates it with a probability Pm = mutationProbability.
* */
public class GeneMutator implements Mutator {

    private final double mutationProbability;

    public GeneMutator(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(GameClass individualToMutate, MutatorManager mutatorManager) throws IOException {
        int randomGene = (int) Math.ceil(Utils.getRandomInRange(0, individualToMutate.getEquipment().size()));
        double rand = Utils.getRandomInRange(0.0, 1.0);
        if (rand < mutationProbability) {
            mutatorManager.mutateGene(individualToMutate, randomGene);
            individualToMutate.setAttributes();
        }
    }
}
