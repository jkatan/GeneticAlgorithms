package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/*
 * El cruce se hace asumiendo la siguiente disposicion de los genes (los cuales vienen asi porque asi instanciamos a cada individuo):
 *
 * altura | arma | bota | casco | guante | pechera
 *
 * Esta clase vendria a ser como una clase de "utilities" para los metodos de crossover
 * */

public class CrossoverManager {

    private final Reproductor reproductor;

    public CrossoverManager(Reproductor reproductor) {
        this.reproductor = reproductor;
    }

    public List<GameClass> cross(List<GameClass> parents) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        List<GameClass> parentsCopy = new ArrayList<>(parents);
        List<GameClass> children = new ArrayList<>();

        while (!parentsCopy.isEmpty()) {
            GameClass parent1 = pickRandomParent(parentsCopy);
            parentsCopy.remove(parent1);

            GameClass parent2 = pickRandomParent(parentsCopy);
            parentsCopy.remove(parent2);

            children.addAll(this.reproductor.cross(parent1, parent2));
        }

        return children;
    }

    public static void exchangeGenesInLocusRange(GameClass parent1, GameClass child1, GameClass parent2,
                                                 GameClass child2, int startLocus, int finishLocus) {
        for (int i=startLocus; i<=finishLocus; i++) {
            exchangeGenes(parent1, child1, parent2, child2, i);
        }
    }

    public static List<GameClass> initializeChildren(GameClass parent1, GameClass parent2) throws
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<GameClass> children = new ArrayList<>();
        GameClass child1 = instantiateNewChild(parent1);
        GameClass child2 = instantiateNewChild(parent2);
        children.add(child1);
        children.add(child2);
        return children;
    }

    public static void printParentsAndChildrenInformation(GameClass parent1, GameClass child1, GameClass parent2,
                                                          GameClass child2) {
        System.out.println("Parent 1: ");
        System.out.println(parent1);
        System.out.println("Child 1: ");
        System.out.println(child1);

        System.out.println("Parent 2: ");
        System.out.println(parent2);
        System.out.println("Child 2");
        System.out.println(child2);
    }

    public static void exchangeGenes(GameClass parent1, GameClass child1, GameClass parent2, GameClass child2,
                               int locusPosition) {
        if (locusPosition == 0) {
            child1.setHeight(parent2.getHeight());
            child2.setHeight(parent1.getHeight());
        } else {
            child1.getEquipment().set(locusPosition-1, parent2.getEquipment().get(locusPosition-1));
            child2.getEquipment().set(locusPosition-1, parent1.getEquipment().get(locusPosition-1));
        }
    }

    private static GameClass instantiateNewChild(GameClass parent) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        return parent.getClass().getConstructor(Double.TYPE, List.class)
                .newInstance(parent.getHeight(), new ArrayList<>(parent.getEquipment()));
    }

    private GameClass pickRandomParent(List<GameClass> parents) {
        int randomIndex = (int) Math.ceil(Utils.getRandomInRange(0.0, parents.size() - 1));
        return parents.get(randomIndex);
    }
}
