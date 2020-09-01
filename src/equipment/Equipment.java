package equipment;

public class Equipment {
    private final double strength;
    private final double agility;
    private final double expertise;
    private final double resistance;
    private final double health;

    public Equipment(double strength, double agility, double expertise, double resistance, double health) {
        this.strength = strength;
        this.agility = agility;
        this.expertise = expertise;
        this.resistance = resistance;
        this.health = health;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getExpertise() {
        return expertise;
    }

    public double getResistance() {
        return resistance;
    }

    public double getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "strength=" + strength +
                ", agility=" + agility +
                ", expertise=" + expertise +
                ", resistance=" + resistance +
                ", health=" + health +
                '}';
    }
}
