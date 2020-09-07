package GeneticComponents.Implementations.Mutators;

import GeneticComponents.Interfaces.Mutator;
import Utils.Utils;
import classes.GameClass;

import java.io.IOException;

public class CompleteMutator implements Mutator {

    private final double mutationProbability;

    public CompleteMutator(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(GameClass individualToMutate, MutatorManager mutatorManager) throws IOException {
        double rand = Utils.getRandomInRange(0.0, 1.0);
        int genesQuantity = individualToMutate.getEquipment().size();

        if (rand < mutationProbability) {
            for (int i=0; i<=genesQuantity; i++) {
                mutatorManager.mutateGene(individualToMutate, i);
            }

            individualToMutate.setAttributes();
        }
    }
}
