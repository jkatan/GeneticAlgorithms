package classes;

import equipment.Equipment;

import java.util.LinkedList;
import java.util.List;

public abstract class GameClass {
    protected double height;
    protected List<Equipment> equipment;
    protected double attack;
    protected double attackModifier;
    protected double defense;
    protected double defenseModifier;

    public GameClass(double height) {
        this(height, new LinkedList<>());
    }

    public GameClass(double height, List<Equipment> equipment) {
        this.height = height;
        this.equipment = equipment;
        this.attack = 0;
        this.attackModifier = 0.7 - Math.pow(3 * height - 5, 4) + Math.pow(3 * height - 5, 2) + height / 4;
        this.defense = 0;
        this.defenseModifier = 1.9 + Math.pow(2.5 * height - 4.16, 4) - Math.pow(2.5 * height - 4.16, 2) - 3 * height / 10;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setAttributes() {
        setAttributesFromEquipmentList(equipment, attack, defense);
    }

    public void setAttributesFromEquipmentList(List<Equipment> equipmentList, double attack, double defense) {
        // todo
    }

    public abstract double getBestPerformance();
}
