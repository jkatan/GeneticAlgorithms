package equipment;

public class Equipment {
    private final double strength;
    private final double agility;
    private final double expertise;
    private final double resistance;
    private final double health;
    private final int id;

    public Equipment(double strength, double agility, double expertise, double resistance, double health, int id) {
        this.strength = strength;
        this.agility = agility;
        this.expertise = expertise;
        this.resistance = resistance;
        this.health = health;
        this.id = id;
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

    public int getId() {
        return id;
    }

    /* Return only item id for more clarity when reading program output */
    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
