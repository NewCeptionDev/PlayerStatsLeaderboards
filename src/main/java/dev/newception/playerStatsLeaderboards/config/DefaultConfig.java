package dev.newception.playerStatsLeaderboards.config;

import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DefaultConfig extends Config {

    public DefaultConfig() {
        super(getDefaultConfigDefaultDisplayedItem(), getDefaultConfigDisplayedItem(), getDefaultConfigCustomTranslation());
    }

    private static Identifier getDefaultConfigDefaultDisplayedItem() {
        return Registries.ITEM.getId(Items.GRASS_BLOCK);
    }

    private static Map<Identifier, Identifier> getDefaultConfigDisplayedItem() {
        Map<Identifier, Identifier> displayedItem = new HashMap<>();

        displayedItem.put(Stats.PLAY_TIME, Registries.ITEM.getId(Items.CLOCK));

        return displayedItem;
    }

    private static Map<Identifier, String> getDefaultConfigCustomTranslation() {
        return new HashMap<>();
    }
}
