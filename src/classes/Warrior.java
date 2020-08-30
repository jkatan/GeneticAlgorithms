package classes;

import equipment.Equipment;

import java.util.List;

public class Warrior extends GameClass {

    public Warrior(double height) {
        super(height);
    }

    public Warrior(double height, List<Equipment> equipment) {
        super(height, equipment);
    }

    @Override
    public double getBestPerformance() {
        return 0.6 * attack + 0.6 * defense;
    }
}
