package classes;

import equipment.Equipment;

import java.util.List;

public class Infiltrator extends GameClass {

    public Infiltrator(double height) {
        super(height);
    }

    public Infiltrator(double height, List<Equipment> equipment) {
        super(height, equipment);
    }

    @Override
    public double getBestPerformance() {
        return 0.8 * attack + 0.3 * defense;
    }
}
