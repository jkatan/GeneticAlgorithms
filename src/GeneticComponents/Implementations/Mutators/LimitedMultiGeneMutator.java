package GeneticComponents.Implementations.Mutators;

import GeneticComponents.Interfaces.Mutator;
import Utils.Utils;
import classes.GameClass;
import equipment.Equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* Picks a random amount of genes to mutate, and then mutate those genes with probability Pm
* */
public class LimitedMultiGeneMutator implements Mutator {

    private final double mutationProbability;

    public LimitedMultiGeneMutator(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(GameClass individualToMutate, MutatorManager mutatorManager) throws IOException {
        int randomGenesQuantity = (int) Math.ceil(Utils.getRandomInRange(1, individualToMutate.getEquipment().size()));
        double rand;
        int randomGene;
        for (int i=0; i<=randomGenesQuantity; i++) {
            randomGene = (int) Math.ceil(Utils.getRandomInRange(0, individualToMutate.getEquipment().size()));
            rand = Utils.getRandomInRange(0.0, 1.0);
            if (rand < mutationProbability) {
                mutatorManager.mutateGene(individualToMutate, randomGene);
            }
        }

        individualToMutate.setAttributes();
    }
}
