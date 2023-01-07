package dev.newception.playerStatsLeaderboards.util;

import net.minecraft.item.Item;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;

public enum ItemStatsType {

    BROKEN("minecraft:broken"),
    CRAFTED("minecraft:crafted"),
    USED("minecraft:used"),
    PICKED_UP("minecraft:picked_up"),
    DROPPED("minecraft:dropped");

    private final String identifier;

    ItemStatsType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static ItemStatsType fromStatType(StatType type) {
        if(type == Stats.BROKEN) {
            return BROKEN;
        }
        if(type == Stats.CRAFTED) {
            return CRAFTED;
        }
        if(type == Stats.DROPPED) {
            return DROPPED;
        }
        if(type == Stats.USED) {
            return USED;
        }
        if(type == Stats.PICKED_UP) {
            return PICKED_UP;
        }

        return null;
    }

    public StatType<Item> toStatType() {
        return switch (this) {
            case BROKEN -> Stats.BROKEN;
            case USED -> Stats.USED;
            case CRAFTED -> Stats.CRAFTED;
            case DROPPED -> Stats.DROPPED;
            case PICKED_UP -> Stats.PICKED_UP;
        };

    }
}
