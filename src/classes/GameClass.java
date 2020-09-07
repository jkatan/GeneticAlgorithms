package classes;

import equipment.Equipment;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        this.attackModifier = 0.7 - Math.pow(3 * height - 5, 4) + Math.pow(3 * height - 5, 2) + height / 4;
        this.defenseModifier = 1.9 + Math.pow(2.5 * height - 4.16, 4) - Math.pow(2.5 * height - 4.16, 2) - 3 * height / 10;
        setAttributes();
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return this.height;
    }

    public void setAttack(double strength, double agility, double expertise) {
        this.attack = (agility + expertise) * strength * attackModifier;
    }

    public void setDefense(double health, double resistance, double expertise) {
        this.defense = (resistance + expertise) * health * defenseModifier;
    }

    public void setAttributes() {
        setAttributesFromEquipmentList(equipment);
    }

    private void setAttributesFromEquipmentList(List<Equipment> equipmentList) {
        double strength = 0, agility = 0, expertise = 0, resistance = 0, health = 0;
        for (Equipment e: equipmentList) {
            strength += e.getStrength();
            agility += e.getAgility();
            expertise += e.getExpertise();
            resistance += e.getResistance();
            health += e.getHealth();
        }
        strength = 100 * Math.tanh(0.01 * strength);
        agility = Math.tanh(0.01 * agility);
        expertise = 0.6 * Math.tanh(0.01 * expertise);
        resistance = Math.tanh(0.01 * resistance);
        health = 100 * Math.tanh(0.01 * health);

        setAttack(strength, agility, expertise);
        setDefense(health, resistance, expertise);
    }

    @Override
    public String toString() {
        return "GameClass{" +
                "height=" + height +
                ", equipment=" + equipment +
                ", attack=" + attack +
                ", attackModifier=" + attackModifier +
                ", defense=" + defense +
                ", defenseModifier=" + defenseModifier +
                '}';
    }

    public abstract double getBestPerformance();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameClass gameClass = (GameClass) o;
        return Math.abs(gameClass.height - height) <= 0.2 &&
                Math.abs(gameClass.attack - attack) <= 0.5 &&
                Math.abs(gameClass.attackModifier - attackModifier) <= 0.5 &&
                Math.abs(gameClass.defense - defense) <= 0.5 &&
                Math.abs(gameClass.defenseModifier - defenseModifier) <= 0.5;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, equipment, attack, attackModifier, defense, defenseModifier);
    }
}
