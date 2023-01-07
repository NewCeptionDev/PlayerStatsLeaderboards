package dev.newception.playerStatsLeaderboards.service;

import dev.newception.playerStatsLeaderboards.util.ItemStatsType;
import dev.newception.playerStatsLeaderboards.util.PlayerInformationCache;
import dev.newception.playerStatsLeaderboards.util.PlayerStatValue;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.util.*;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.PLAYER_INFORMATION_CACHE;
import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.REGISTERED_PLAYERS;

public class LeaderboardDataBuilder {

    public static List<PlayerStatValue> buildLeaderboardDataGeneralStatistic(ServerCommandSource source, Stat<Identifier> stat) {
        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        REGISTERED_PLAYERS.forEach(playerUUID -> {
            String playerName;
            Integer statValue;
            if (source.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(stat);
            } else {
                if(!PLAYER_INFORMATION_CACHE.containsKey(playerUUID)) {
                    PLAYER_INFORMATION_CACHE.put(playerUUID, new PlayerInformationCache(playerUUID));
                }

                playerName = PLAYER_INFORMATION_CACHE.get(playerUUID).getUsername();
                statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getGeneralStat(stat.getValue(), source.getServer().getSavePath(WorldSavePath.STATS));
            }

            if(playerName != null && statValue != null) {
                playerStatValueList.add(new PlayerStatValue(playerName, statValue));
            }
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        return playerStatValueList;
    }

    public static Map<ItemStatsType, List<PlayerStatValue>> buildLeaderboardDataItemStatistic(ServerCommandSource source, Item item) {
        Map<ItemStatsType, List<PlayerStatValue>> playerStatValuesForAllItemStatistics = new HashMap<>();

//        playerStatValuesForAllItemStatistics.put("MINED", getLeaderboardDataForItemStatistic(source, Stats.MINED, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.BROKEN, getLeaderboardDataForItemStatistic(source, Stats.BROKEN, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.CRAFTED, getLeaderboardDataForItemStatistic(source, Stats.CRAFTED, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.USED, getLeaderboardDataForItemStatistic(source, Stats.USED, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.PICKED_UP, getLeaderboardDataForItemStatistic(source, Stats.PICKED_UP, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.DROPPED, getLeaderboardDataForItemStatistic(source, Stats.DROPPED, item));

        return playerStatValuesForAllItemStatistics;
    }

    private static List<PlayerStatValue> getLeaderboardDataForItemStatistic(ServerCommandSource source, StatType<Item> statType, Item item) {
        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        REGISTERED_PLAYERS.forEach(playerUUID -> {
            String playerName;
            Integer statValue;
            if (source.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(statType, item);
            } else {
                if(!PLAYER_INFORMATION_CACHE.containsKey(playerUUID)) {
                    PLAYER_INFORMATION_CACHE.put(playerUUID, new PlayerInformationCache(playerUUID));
                }

                playerName = PLAYER_INFORMATION_CACHE.get(playerUUID).getUsername();
                statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getItemStat(ItemStatsType.fromStatType(statType), Registries.ITEM.getId(item), source.getServer().getSavePath(WorldSavePath.STATS));
            }

            if(playerName != null && statValue != null) {
                playerStatValueList.add(new PlayerStatValue(playerName, statValue));
            }
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        return playerStatValueList;
    }
}
