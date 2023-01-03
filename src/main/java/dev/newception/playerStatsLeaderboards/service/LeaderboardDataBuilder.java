package dev.newception.playerStatsLeaderboards.service;

import dev.newception.playerStatsLeaderboards.util.PlayerInformationCache;
import dev.newception.playerStatsLeaderboards.util.PlayerStatValue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.PLAYER_INFORMATION_CACHE;
import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.REGISTERED_PLAYERS;

public class LeaderboardDataBuilder {

    public static List<PlayerStatValue> buildLeaderboardData(ServerCommandSource source, Stat<Identifier> stat) {
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
                statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getStat(stat.getValue(), source.getServer().getSavePath(WorldSavePath.STATS));
            }

            if(playerName != null && statValue != null) {
                playerStatValueList.add(new PlayerStatValue(playerName, statValue));
            }
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        return playerStatValueList;
    }
}
