package dev.newception.playerStatsLeaderboards.util;

import net.minecraft.entity.EntityType;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;

public enum MobStatsType {

    KILLED("minecraft:killed"),
    KILLED_BY("minecraft:killed_by");

    private final String identifier;

    MobStatsType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static MobStatsType fromStatType(StatType type) {
        if(type == Stats.KILLED) {
            return KILLED;
        }
        if(type == Stats.KILLED_BY) {
            return KILLED_BY;
        }

        return null;
    }

    public StatType<EntityType<?>> toStatType() {
        return switch (this) {
            case KILLED -> Stats.KILLED;
            case KILLED_BY -> Stats.KILLED_BY;
        };

    }
}
