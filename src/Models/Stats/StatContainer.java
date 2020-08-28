package Models.Stats;

public class StatContainer implements BaseStatsQueryable {
    // Fuerza
    private float strength;
    // Pericia
    private float expertise;
    // Agilidad
    private float agility;
    // Resistencia
    private float resistance;
    // Vida
    private float health;

    public StatContainer(float strength, float expertise, float agility, float resistance, float health) {
        this.strength = strength;
        this.expertise = expertise;
        this.agility = agility;
        this.resistance = resistance;
        this.health = health;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setExpertise(float expertise) {
        this.expertise = expertise;
    }

    public void setAgility(float agility) {
        this.agility = agility;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public double getStrength() {
        return strength;
    }

    public double getExpertise() {
        return expertise;
    }

    public double getAgility() {
        return agility;
    }

    public double getResistance() {
        return resistance;
    }

    public double getHealth() {
        return health;
    }
}
