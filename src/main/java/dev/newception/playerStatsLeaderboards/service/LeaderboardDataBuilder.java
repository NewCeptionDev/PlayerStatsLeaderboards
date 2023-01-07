package dev.newception.playerStatsLeaderboards.service;

import dev.newception.playerStatsLeaderboards.util.ItemStatsType;
import dev.newception.playerStatsLeaderboards.util.MobStatsType;
import dev.newception.playerStatsLeaderboards.util.PlayerInformationCache;
import dev.newception.playerStatsLeaderboards.util.PlayerStatValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
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

        Block correspondingBlock = Registries.BLOCK.get(Registries.ITEM.getId(item));
        if(!correspondingBlock.equals(Blocks.AIR)) {
            playerStatValuesForAllItemStatistics.put(ItemStatsType.MINED, getLeaderboardDataForItemStatistic(source, Stats.MINED, correspondingBlock));
        }

        playerStatValuesForAllItemStatistics.put(ItemStatsType.BROKEN, getLeaderboardDataForItemStatistic(source, Stats.BROKEN, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.CRAFTED, getLeaderboardDataForItemStatistic(source, Stats.CRAFTED, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.USED, getLeaderboardDataForItemStatistic(source, Stats.USED, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.PICKED_UP, getLeaderboardDataForItemStatistic(source, Stats.PICKED_UP, item));
        playerStatValuesForAllItemStatistics.put(ItemStatsType.DROPPED, getLeaderboardDataForItemStatistic(source, Stats.DROPPED, item));

        return playerStatValuesForAllItemStatistics;
    }

    public static Map<MobStatsType, List<PlayerStatValue>> buildLeaderboardDataMobStatistic(ServerCommandSource source, EntityType entityType) {
        Map<MobStatsType, List<PlayerStatValue>> playerStatValuesForAllMobStatistics = new HashMap<>();

        playerStatValuesForAllMobStatistics.put(MobStatsType.KILLED, getLeaderboardDataForMobStatistic(source, Stats.KILLED, entityType));
        playerStatValuesForAllMobStatistics.put(MobStatsType.KILLED_BY, getLeaderboardDataForMobStatistic(source, Stats.KILLED_BY, entityType));

        return playerStatValuesForAllMobStatistics;
    }

    private static <T> List<PlayerStatValue> getLeaderboardDataForItemStatistic(ServerCommandSource source, StatType<T> statType, T itemStatisticElement) {
        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        REGISTERED_PLAYERS.forEach(playerUUID -> {
            String playerName;
            Integer statValue = null;
            if (source.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(statType, itemStatisticElement);
            } else {
                if(!PLAYER_INFORMATION_CACHE.containsKey(playerUUID)) {
                    PLAYER_INFORMATION_CACHE.put(playerUUID, new PlayerInformationCache(playerUUID));
                }

                playerName = PLAYER_INFORMATION_CACHE.get(playerUUID).getUsername();
                if(itemStatisticElement instanceof Item) {
                    statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getItemStat(ItemStatsType.fromStatType(statType), Registries.ITEM.getId((Item) itemStatisticElement), source.getServer().getSavePath(WorldSavePath.STATS));
                } else if(itemStatisticElement instanceof Block) {
                    statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getItemStat(ItemStatsType.fromStatType(statType), Registries.BLOCK.getId((Block) itemStatisticElement), source.getServer().getSavePath(WorldSavePath.STATS));
                }
            }

            if(playerName != null && statValue != null) {
                playerStatValueList.add(new PlayerStatValue(playerName, statValue));
            }
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        return playerStatValueList;
    }

    private static List<PlayerStatValue> getLeaderboardDataForMobStatistic(ServerCommandSource source, StatType<EntityType<?>> statType, EntityType entityType) {
        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        REGISTERED_PLAYERS.forEach(playerUUID -> {
            String playerName;
            Integer statValue;
            if (source.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(statType, entityType);
            } else {
                if(!PLAYER_INFORMATION_CACHE.containsKey(playerUUID)) {
                    PLAYER_INFORMATION_CACHE.put(playerUUID, new PlayerInformationCache(playerUUID));
                }

                playerName = PLAYER_INFORMATION_CACHE.get(playerUUID).getUsername();
                statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getMobStat(MobStatsType.fromStatType(statType), entityType, source.getServer().getSavePath(WorldSavePath.STATS));
            }

            if(playerName != null && statValue != null) {
                playerStatValueList.add(new PlayerStatValue(playerName, statValue));
            }
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        return playerStatValueList;
    }
}
