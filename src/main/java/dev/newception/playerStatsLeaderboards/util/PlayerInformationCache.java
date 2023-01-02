package dev.newception.playerStatsLeaderboards.util;

import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInformationCache {

    private UUID playerUUID;

    private String username;

    private Map<Identifier, Integer> statCache;

    public PlayerInformationCache(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.statCache = new HashMap<>();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getUsername() {
        if(username == null) {
            findUsername();
        }

        return username;
    }

    public int getStat(Identifier requestedStatIdentifier, Path statsSavePath) {
        if(!statCache.containsKey(requestedStatIdentifier)) {
            statCache.put(requestedStatIdentifier, StatsFileReader.readStatForPlayer(playerUUID, requestedStatIdentifier.getPath(), statsSavePath));
        }

        return statCache.get(requestedStatIdentifier);
    }

    public void findUsername() {
        try {
            username = UsernameFileReader.readDisplayNameOfPlayer(playerUUID);
        } catch (RuntimeException ignored) {
            username = UsernameAPI.getUsernameForUUID(playerUUID);
        }
    }
}
