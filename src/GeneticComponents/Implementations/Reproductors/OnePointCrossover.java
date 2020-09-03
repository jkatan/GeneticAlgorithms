package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

/*
* El cruce se hace asumiendo la siguiente disposicion de los genes (los cuales vienen asi porque asi instanciamos a cada individuo):
*
* altura | arma | bota | casco | guante | pechera
*
* */

public class OnePointCrossover implements Reproductor {

    @Override
    public List<GameClass> cross(GameClass parent1, GameClass parent2) {
        GameClass child1 = null;
        GameClass child2 = null;

        // Hacemos esto para instanciar al hijo de forma generica (y porque GameClass es abstract), independientemente
        // de si es un warrior, defender, infiltrator o archer
        try {
            child1 = parent1.getClass().getConstructor(Double.TYPE, List.class)
                    .newInstance(parent1.getHeight(), new ArrayList<>(parent1.getEquipment()));
            child2 = parent2.getClass().getConstructor(Double.TYPE, List.class)
                    .newInstance(parent2.getHeight(), new ArrayList<>(parent2.getEquipment()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int genesQuantity = parent1.getEquipment().size();
        int randomLocus = (int) Math.ceil(Utils.getRandomInRange(0, genesQuantity));

        for (int i=randomLocus; i<=genesQuantity; i++) {
            if (i == 0) {
                child1.setHeight(parent2.getHeight());
                child2.setHeight(parent1.getHeight());
            }
            child1.getEquipment().set(i-1, parent2.getEquipment().get(i-1));
            child2.getEquipment().set(i-1, parent1.getEquipment().get(i-1));
        }

        child1.setAttributes();
        child2.setAttributes();

        List<GameClass> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }
}
