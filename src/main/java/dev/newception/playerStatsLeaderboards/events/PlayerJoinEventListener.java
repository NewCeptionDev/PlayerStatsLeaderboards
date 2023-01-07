package dev.newception.playerStatsLeaderboards.events;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.PLAYER_INFORMATION_CACHE;
import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.REGISTERED_PLAYERS;

public class PlayerJoinEventListener {

    public static void handlePlayerJoinEvent(ServerPlayNetworkHandler handler) {
        ServerPlayerEntity player = handler.getPlayer();

        REGISTERED_PLAYERS.add(player.getUuid());

        if (PLAYER_INFORMATION_CACHE.containsKey(player.getUuid())) {
            PLAYER_INFORMATION_CACHE.get(player.getUuid()).resetStatsCache();
            PLAYER_INFORMATION_CACHE.get(player.getUuid()).updateUsernameIfNecessary(player.getDisplayName().getString());
        }
    }
}
