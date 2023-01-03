package dev.newception.playerStatsLeaderboards.util;

import org.jetbrains.annotations.NotNull;

public record PlayerStatValue(String playerName, int statValue) implements Comparable<PlayerStatValue> {

    @Override
    public int compareTo(@NotNull PlayerStatValue o) {
        return Integer.compare(statValue, o.statValue);
    }
}