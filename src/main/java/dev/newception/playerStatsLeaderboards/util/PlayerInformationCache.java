package dev.newception.playerStatsLeaderboards.util;

import dev.newception.playerStatsLeaderboards.io.StatsFileReader;
import dev.newception.playerStatsLeaderboards.io.UsernameAPI;
import dev.newception.playerStatsLeaderboards.io.UsernameFileReader;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInformationCache {

    private final UUID playerUUID;

    private String username;

    private Map<Identifier, Integer> generalStatCache;

    private Map<ItemStatsType, Map<Identifier, Integer>> itemStatCache;

    private Map<MobStatsType, Map<EntityType, Integer>> mobStatCache;

    public PlayerInformationCache(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.generalStatCache = new HashMap<>();
        this.itemStatCache = new HashMap<>();
        this.mobStatCache = new HashMap<>();
    }

    public String getUsername() {
        if(username == null) {
            findUsername();
        }

        return username;
    }

    public Integer getGeneralStat(Identifier requestedStatIdentifier, Path statsSavePath) {
        if(!generalStatCache.containsKey(requestedStatIdentifier)) {
            generalStatCache.put(requestedStatIdentifier, StatsFileReader.readGeneralStatForPlayer(playerUUID, requestedStatIdentifier.getNamespace() + ":" + requestedStatIdentifier.getPath(), statsSavePath));
        }

        return generalStatCache.get(requestedStatIdentifier);
    }

    public Integer getItemStat(ItemStatsType requestedType, Identifier relevantItem, Path statsSavePath) {
        if(!itemStatCache.containsKey(requestedType)) {
            itemStatCache.put(requestedType, new HashMap<>());
        }

        if(!itemStatCache.get(requestedType).containsKey(relevantItem)) {
            itemStatCache.get(requestedType).put(relevantItem, StatsFileReader.readNonGeneralStatForPlayer(playerUUID, requestedType.getIdentifier(), relevantItem.getNamespace() + ":" + relevantItem.getPath(), statsSavePath));
        }

        return itemStatCache.get(requestedType).get(relevantItem);
    }

    public Integer getMobStat(MobStatsType requestedType, EntityType entityType, Path statsSavePath) {
        if(!mobStatCache.containsKey(requestedType)) {
            mobStatCache.put(requestedType, new HashMap<>());
        }

        if(!mobStatCache.get(requestedType).containsKey(entityType)) {
            mobStatCache.get(requestedType).put(entityType, StatsFileReader.readNonGeneralStatForPlayer(playerUUID, requestedType.getIdentifier(), "minecraft:" + entityType.getUntranslatedName(), statsSavePath));
        }

        return mobStatCache.get(requestedType).get(entityType);
    }

    public void findUsername() {
        username = UsernameFileReader.readDisplayNameOfPlayer(playerUUID);

        if(username == null) {
            username = UsernameAPI.getUsernameForUUID(playerUUID);
        }
    }

    public void resetStatsCache() {
        this.generalStatCache = new HashMap<>();
        this.itemStatCache = new HashMap<>();
        this.mobStatCache = new HashMap<>();
    }

    public void updateUsernameIfNecessary(String username) {
        if(!this.username.equals(username)) {
            this.username = username;
        }
    }
}
