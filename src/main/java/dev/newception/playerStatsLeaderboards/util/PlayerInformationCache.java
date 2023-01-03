package dev.newception.playerStatsLeaderboards.util;

import dev.newception.playerStatsLeaderboards.io.StatsFileReader;
import dev.newception.playerStatsLeaderboards.io.UsernameAPI;
import dev.newception.playerStatsLeaderboards.io.UsernameFileReader;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInformationCache {

    private final UUID playerUUID;

    private String username;

    private Map<Identifier, Integer> statCache;

    public PlayerInformationCache(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.statCache = new HashMap<>();
    }

    public String getUsername() {
        if(username == null) {
            findUsername();
        }

        return username;
    }

    public Integer getStat(Identifier requestedStatIdentifier, Path statsSavePath) {
        if(!statCache.containsKey(requestedStatIdentifier)) {
            statCache.put(requestedStatIdentifier, StatsFileReader.readStatForPlayer(playerUUID, requestedStatIdentifier.getPath(), statsSavePath));
        }

        return statCache.get(requestedStatIdentifier);
    }

    public void findUsername() {
        username = UsernameFileReader.readDisplayNameOfPlayer(playerUUID);

        if(username == null) {
            username = UsernameAPI.getUsernameForUUID(playerUUID);
        }
    }

    public void resetStatsCache() {
        this.statCache = new HashMap<>();
    }

    public void updateUsernameIfNecessary(String username) {
        if(!this.username.equals(username)) {
            this.username = username;
        }
    }
}
