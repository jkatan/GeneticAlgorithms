package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class UniformCrossover implements Reproductor {

    private static final double geneExchangeProbability = 0.5;

    @Override
    public List<GameClass> cross(GameClass parent1, GameClass parent2) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<GameClass> children = CrossoverManager.initializeChildren(parent1, parent2);

        int locusAmount = parent1.getEquipment().size();
        GameClass child1 = children.get(0);
        GameClass child2 = children.get(1);
        double randomNumber;
        for (int i=0; i<=locusAmount; i++) {
            randomNumber = Utils.getRandomInRange(0.0, 1.0);
            if (randomNumber < geneExchangeProbability) {
                CrossoverManager.exchangeGenes(parent1, child1, parent2, child2, i);
            }
        }

        child1.setAttributes();
        child2.setAttributes();

        return children;
    }
}
