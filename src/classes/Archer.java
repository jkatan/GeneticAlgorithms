package classes;

import equipment.Equipment;

import java.util.List;

public class Archer extends GameClass {

    public Archer(double height) {
        super(height);
    }

    public Archer(double height, List<Equipment> equipment) {
        super(height, equipment);
    }

    @Override
    public double getBestPerformance() {
        return 0.9 * attack + 0.1 * defense;
    }
}
