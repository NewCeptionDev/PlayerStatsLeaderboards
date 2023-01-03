package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.newception.playerStatsLeaderboards.service.LeaderboardBookBuilder;
import dev.newception.playerStatsLeaderboards.service.LeaderboardDataBuilder;
import dev.newception.playerStatsLeaderboards.service.StatService;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
            source.sendMessage(Text.literal("The given identifier is not a statistic.").formatted(Formatting.RED));
            return 0;
        }

        Stat<Identifier> stat = StatService.findStatForIdentifier(statIdentifier);

        if(stat == null) {
            source.sendMessage(Text.literal("No Stat found for the given identifier.").formatted(Formatting.RED));
            return -1;
        }

        BookGui gui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBook(LeaderboardDataBuilder.buildLeaderboardData(source, stat), stat));

        gui.open();

        return 0;
    }
}
