package dev.newception.playerStatsLeaderboards.util;

import org.jetbrains.annotations.NotNull;

public class PlayerStatValue implements Comparable<PlayerStatValue> {
    private final String playerName;
    private final int statValue;

    public PlayerStatValue(String playerName, int statValue) {
        this.playerName = playerName;
        this.statValue = statValue;
    }

    @Override
    public int compareTo(@NotNull PlayerStatValue o) {
        return Integer.compare(statValue, o.statValue);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getStatValue() {
        return statValue;
    }
}