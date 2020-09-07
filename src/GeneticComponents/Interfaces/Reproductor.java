package GeneticComponents.Interfaces;

import classes.GameClass;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Reproductor {

    List<GameClass> cross(GameClass parent1, GameClass parent2) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
