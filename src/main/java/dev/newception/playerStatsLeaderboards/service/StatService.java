package dev.newception.playerStatsLeaderboards.service;

import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class StatService {

    public static Stat<Identifier> findStatForIdentifier(Identifier identifier) {
        for (Stat<Identifier> stat : Stats.CUSTOM) {
            if (stat.getValue().getPath().equals(identifier.getPath())) {
                return stat;
            }
        }

        return null;
    }

}
