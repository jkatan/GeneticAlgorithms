package Models.Characters;

import Models.Items.Item;
import Models.Stats.StatContainer;

import java.util.List;

public class Defender extends Character {

    private static final double PERFORMANCE_ATTACK_MODIFIER = 0.3;
    private static final double PERFORMANCE_DEFENSE_MODIFIER = 0.8;

    public Defender(double height, List<Item> equipment) {
        super(height, equipment, PERFORMANCE_ATTACK_MODIFIER, PERFORMANCE_DEFENSE_MODIFIER);
    }
}
