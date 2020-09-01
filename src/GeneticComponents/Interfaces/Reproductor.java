package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface Reproductor {

    List<GameClass> cross(List<GameClass> parents);
}
