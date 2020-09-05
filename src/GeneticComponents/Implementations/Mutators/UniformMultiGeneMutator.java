package GeneticComponents.Implementations.Mutators;

import GeneticComponents.Interfaces.Mutator;
import Utils.Utils;
import classes.GameClass;

import java.io.IOException;

public class UniformMultiGeneMutator implements Mutator {

    private final double mutationProbability;

    public UniformMultiGeneMutator(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(GameClass individualToMutate, MutatorManager mutatorManager) throws IOException {
        int genesQuantity = individualToMutate.getEquipment().size();
        for (int i=0; i<=genesQuantity; i++) {
            double rand = Utils.getRandomInRange(0.0, 1.0);
            if (rand < mutationProbability) {
                mutatorManager.mutateGene(individualToMutate, i);
            }
        }

        individualToMutate.setAttributes();
    }
}
