package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AnnularCrossover implements Reproductor {
    @Override
    public List<GameClass> cross(GameClass parent1, GameClass parent2) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<GameClass> children = CrossoverManager.initializeChildren(parent1, parent2);
        int genesQuantity = parent1.getEquipment().size();
        int startingLocus = (int) Math.ceil(Utils.getRandomInRange(0, genesQuantity-1));
        int genesToExchange = (int) Math.ceil(Utils.getRandomInRange(0, (int)(Math.ceil(genesQuantity/2.0))));
        int exchangedGenes = 0;
        int locusPositionToChange;

        GameClass child1 = children.get(0);
        GameClass child2 = children.get(1);

        while (exchangedGenes < genesToExchange) {
            if (startingLocus + exchangedGenes <= genesQuantity) {
                locusPositionToChange = startingLocus + exchangedGenes;
            } else {
                locusPositionToChange = genesToExchange - exchangedGenes - 1;
            }
            CrossoverManager.exchangeGenes(parent1, child1, parent2, child2,locusPositionToChange);
            exchangedGenes++;
        }

        child1.setAttributes();
        child2.setAttributes();

        return children;
    }
}
