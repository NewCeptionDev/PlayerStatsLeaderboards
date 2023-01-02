package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.newception.playerStatsLeaderboards.util.*;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShowLeaderboardCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("leaderboards")
                .then(argument("stat", IdentifierArgumentType.identifier())
                        .executes(context -> showLeaderboard(context.getSource(), IdentifierArgumentType.getIdentifier(context, "stat")))));

    }

    private static int showLeaderboard(ServerCommandSource source, Identifier statIdentifier) {
        if (!Registries.CUSTOM_STAT.containsId(statIdentifier)) {
            source.sendMessage(Text.literal("§4The given identifier is not a statistic."));
            return 0;
        }

        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        Stat<Identifier> leaderboardStat = null;

        for (Stat<Identifier> stat : Stats.CUSTOM) {
            if (stat.getValue().getPath().equals(statIdentifier.getPath())) {
                leaderboardStat = stat;
                break;
            }
        }

        if (leaderboardStat == null) {
            source.sendMessage(Text.literal("§4No Stat found for the given identifier."));
            return -1;
        }

        Stat<Identifier> finalLeaderboardStat = leaderboardStat;
        REGISTERED_PLAYERS.forEach(playerUUID -> {
            String playerName;
            int statValue;
            if (source.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(finalLeaderboardStat);
            } else {
                if(!PLAYER_INFORMATION_CACHE.containsKey(playerUUID)) {
                    PLAYER_INFORMATION_CACHE.put(playerUUID, new PlayerInformationCache(playerUUID));
                }

                playerName = PLAYER_INFORMATION_CACHE.get(playerUUID).getUsername();
                statValue = PLAYER_INFORMATION_CACHE.get(playerUUID).getStat(finalLeaderboardStat.getValue(), source.getServer().getSavePath(WorldSavePath.STATS));
            }
            playerStatValueList.add(new PlayerStatValue(playerName, statValue));
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        BookGui gui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBook(playerStatValueList, leaderboardStat));

        gui.open();

        return 0;
    }
}
