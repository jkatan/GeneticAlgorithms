package Models.Characters;

import Models.Items.Item;
import Models.Stats.BaseStatsQueryable;
import Models.Stats.StatContainer;

import java.util.List;

public abstract class Character implements BaseStatsQueryable {

    private final List<Item> equipment;
    private final double performanceAttackModifier;
    private final double performanceDefenseModifier;

    private final double height;
    private final double heightAttackModifier;
    private final double heightDefenseModifier;

    public Character(double height, List<Item> equipment, double performanceAttackModifier,
                     double performanceDefenseModifier) {
        this.height = height;
        this.equipment = equipment;
        this.performanceAttackModifier = performanceAttackModifier;
        this.performanceDefenseModifier = performanceDefenseModifier;
        this.heightAttackModifier = calculateHeightAttackModifier();
        this.heightDefenseModifier = calculateHeightDefenseModifier();
    }

    public double getPerformance() {
        return performanceAttackModifier * getAttack() + performanceDefenseModifier * getDefense();
    }

    @Override
    public double getStrength() {
        double totalItemStrength = 0.0;
        for (final Item item : equipment) {
            totalItemStrength += item.getItemStats().getStrength();
        }

        return 100 * Math.tanh(0.01 * totalItemStrength);
    }

    @Override
    public double getExpertise() {
        double totalItemExpertise = 0.0;
        for (final Item item : equipment) {
            totalItemExpertise += item.getItemStats().getExpertise();
        }

        return 0.6 * Math.tanh(0.01 * totalItemExpertise);
    }

    @Override
    public double getAgility() {
        double totalItemAgility = 0.0;
        for (final Item item : equipment) {
            totalItemAgility += item.getItemStats().getAgility();
        }

        return Math.tanh(0.01 * totalItemAgility);
    }

    @Override
    public double getResistance() {
        double totalItemResistance = 0.0;
        for (final Item item : equipment) {
            totalItemResistance += item.getItemStats().getResistance();
        }

        return Math.tanh(0.01 * totalItemResistance);
    }

    @Override
    public double getHealth() {
        double totalItemHealth = 0.0;
        for (final Item item : equipment) {
            totalItemHealth += item.getItemStats().getHealth();
        }

        return 100 * Math.tanh(0.01 * totalItemHealth);
    }

    public double getAttack() {
        return (getAgility() + getExpertise()) * getStrength() * heightAttackModifier;
    }

    public double getDefense() {
        return (getResistance() + getExpertise()) * getHealth() * heightDefenseModifier;
    }

    private double calculateHeightAttackModifier() {
        double powerBase = 3 * height - 5;
        return 0.7 - Math.pow(powerBase, 4) + Math.pow(powerBase, 2) + height/4;
    }

    private double calculateHeightDefenseModifier() {
        double powerBase = 2.5 * height - 4.16;
        return 1.9 + Math.pow(powerBase, 4) - Math.pow(powerBase, 2) - (3*height)/10;
    }
}
