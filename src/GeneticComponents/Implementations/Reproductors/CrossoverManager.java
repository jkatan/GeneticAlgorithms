package GeneticComponents.Implementations.Reproductors;

import GeneticComponents.Interfaces.Reproductor;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class CrossoverManager {

    private final Reproductor reproductor;

    public CrossoverManager(Reproductor reproductor) {
        this.reproductor = reproductor;
    }

    public List<GameClass> cross(List<GameClass> parents) {
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

    private GameClass pickRandomParent(List<GameClass> parents) {
        int randomIndex = (int) Math.ceil(Utils.getRandomInRange(0.0, parents.size() - 1));
        return parents.get(randomIndex);
    }
}
