package Models.Items;

import Models.Stats.StatContainer;

public class Item {

    private ItemID itemIdentifier;
    private StatContainer itemStats;

    public Item(ItemID itemIdentifier, StatContainer itemStats) {
        this.itemIdentifier = itemIdentifier;
        this.itemStats = itemStats;
    }

    public void setItemIdentifier(ItemID itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public void setItemStats(StatContainer itemStats) {
        this.itemStats = itemStats;
    }

    public ItemID getItemIdentifier() {
        return itemIdentifier;
    }

    public StatContainer getItemStats() {
        return itemStats;
    }
}
