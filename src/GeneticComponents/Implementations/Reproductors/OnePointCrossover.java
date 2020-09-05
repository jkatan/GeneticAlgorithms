package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class OnePointCrossover implements Reproductor {

    @Override
    public List<GameClass> cross(GameClass parent1, GameClass parent2) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<GameClass> children = CrossoverManager.initializeChildren(parent1, parent2);

        int genesQuantity = parent1.getEquipment().size();
        int randomLocus = (int) Math.ceil(Utils.getRandomInRange(0, genesQuantity));

        GameClass child1 = children.get(0);
        GameClass child2 = children.get(1);

        CrossoverManager.exchangeGenesInLocusRange(parent1, children.get(0), parent2, children.get(1), randomLocus,
                genesQuantity);

        child1.setAttributes();
        child2.setAttributes();
        return children;
    }
}
