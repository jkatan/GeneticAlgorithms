package classes;

import equipment.Equipment;

import java.util.List;

public class Defender extends GameClass {

    public Defender(double height) {
        super(height);
    }

    public Defender(double height, List<Equipment> equipment) {
        super(height, equipment);
    }

    @Override
    public double getBestPerformance() {
        return 0.3 * attack + 0.8 * defense;
    }
}
