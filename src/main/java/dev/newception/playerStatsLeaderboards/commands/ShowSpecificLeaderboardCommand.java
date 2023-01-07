package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.newception.playerStatsLeaderboards.service.ItemService;
import dev.newception.playerStatsLeaderboards.service.LeaderboardBookBuilder;
import dev.newception.playerStatsLeaderboards.service.LeaderboardDataBuilder;
import dev.newception.playerStatsLeaderboards.service.StatService;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShowSpecificLeaderboardCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("leaderboard")
                        .then(literal("general")
                            .then(argument("stat", IdentifierArgumentType.identifier())
                            .executes(context -> showLeaderboardForGeneralStat(context.getSource(), IdentifierArgumentType.getIdentifier(context, "stat"))))));

        dispatcher.register(literal("leaderboard")
                .then(literal("item")
                        .then(argument("item", IdentifierArgumentType.identifier())
                                .executes(context -> showLeaderboardForItemStat(context.getSource(), IdentifierArgumentType.getIdentifier(context, "item"))))));

        dispatcher.register(literal("leaderboard")
                .then(literal("mobs")
                        .then(argument("mobName", StringArgumentType.string())
                                .executes(context -> showLeaderboardForMobStat(context.getSource(), StringArgumentType.getString(context, "mobName"))))));
    }

    private static int showLeaderboardForGeneralStat(ServerCommandSource source, Identifier statIdentifier) {
        if (!Registries.CUSTOM_STAT.containsId(statIdentifier)) {
            source.sendMessage(Text.literal("The given identifier is not a statistic.").formatted(Formatting.RED));
            return 0;
        }

        Stat<Identifier> stat = StatService.findStatForIdentifier(statIdentifier);

        if(stat == null) {
            source.sendMessage(Text.literal("No Statistic found for the given identifier.").formatted(Formatting.RED));
            return -1;
        }

        BookGui gui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBookForGeneralStatistic(LeaderboardDataBuilder.buildLeaderboardDataGeneralStatistic(source, stat), stat));

        gui.open();

        return 0;
    }

    private static int showLeaderboardForItemStat(ServerCommandSource source, Identifier itemIdentifier) {
        if (!Registries.ITEM.containsId(itemIdentifier)) {
            source.sendMessage(Text.literal("The given identifier is not an item.").formatted(Formatting.RED));
            return 0;
        }

        Item item = ItemService.findItemForIdentifier(itemIdentifier);

        if(item == null) {
            source.sendMessage(Text.literal("No Item found for the given identifier.").formatted(Formatting.RED));
            return -1;
        }

        BookGui gui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBookForItemStatistic(LeaderboardDataBuilder.buildLeaderboardDataItemStatistic(source, item), item));

        gui.open();

        return 0;
    }

    private static int showLeaderboardForMobStat(ServerCommandSource source, String entity) {
        if (Registries.ENTITY_TYPE.stream().filter(entityType -> !entityType.getSpawnGroup().getName().equals("misc")).noneMatch(entityType -> entityType.getUntranslatedName().equals(entity))) {
            source.sendMessage(Text.literal("The given String does not represent an entity.").formatted(Formatting.RED));
            return 0;
        }

        Optional<EntityType<?>> entityTypeOptional = Registries.ENTITY_TYPE.stream().filter(type -> type.getUntranslatedName().equals(entity)).findFirst();

        if(entityTypeOptional.isEmpty()) {
            source.sendMessage(Text.literal("No Entity found for the given String.").formatted(Formatting.RED));
            return -1;
        }

        BookGui gui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBookForMobStatistic(LeaderboardDataBuilder.buildLeaderboardDataMobStatistic(source, entityTypeOptional.get()), entityTypeOptional.get()));

        gui.open();

        return 0;
    }
}
