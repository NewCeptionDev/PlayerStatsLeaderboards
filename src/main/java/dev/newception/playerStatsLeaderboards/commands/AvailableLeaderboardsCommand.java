package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.newception.playerStatsLeaderboards.service.LeaderboardBookBuilder;
import dev.newception.playerStatsLeaderboards.service.LeaderboardDataBuilder;
import dev.newception.playerStatsLeaderboards.service.StatService;
import eu.pb4.sgui.api.gui.BookGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.MOD_CONFIG;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AvailableLeaderboardsCommand {
    private static final int SHOWN_ITEMS_PER_PAGE = 10;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("leaderboards")
                .then(literal("list")
                .executes(context -> getAvailableLeaderboards(context.getSource(), 0))));

        dispatcher.register(literal("leaderboards")
                .then(literal("list")
                        .then(argument("page", IntegerArgumentType.integer(1))
                        .executes(context -> getAvailableLeaderboards(context.getSource(), IntegerArgumentType.getInteger(context,"page"))))));

        dispatcher.register(literal("leaderboards")
                .then(literal("show")
                        .executes(context -> getAvailableLeaderboardsGUI(context.getSource(), 0))));

        dispatcher.register(literal("leaderboards")
                .then(literal("show")
                        .then(argument("page", IntegerArgumentType.integer(1))
                        .executes(context -> getAvailableLeaderboardsGUI(context.getSource(), IntegerArgumentType.getInteger(context, "page"))))));
    }

    public static int getAvailableLeaderboards(ServerCommandSource source, int page) {
        int pageCount = Math.floorDiv(Registries.CUSTOM_STAT.size(), SHOWN_ITEMS_PER_PAGE) + 1;
        int startItemIndex = page * SHOWN_ITEMS_PER_PAGE;

        if(page >= pageCount) {
            source.sendMessage(Text.literal("§4The list does not contain that many elements. Please use a smaller page."));
        }

        source.sendMessage(Text.literal("§6The following Statistics are collected and can be used for a leaderboard. (Page " + (page + 1) + "/" + pageCount + ")"));

        for (Identifier identifier : Registries.CUSTOM_STAT.stream().skip(startItemIndex).limit(SHOWN_ITEMS_PER_PAGE).toList()) {
            source.sendMessage(Text.literal("- " + identifier.getPath() + " (").append(Text.translatable(identifier.toTranslationKey("stat"))).append(Text.literal(")")));
        }

        return 0;
    }

    public static int getAvailableLeaderboardsGUI(ServerCommandSource source, int page) {
        int pageCount = Math.floorDiv(Registries.CUSTOM_STAT.size(), 54) + 1;
        int startItemIndex = (page > 0 ? page - 1 : 0) * 54;

        if(page > pageCount) {
            source.sendMessage(Text.literal("There are not that many elements. Please use a smaller page index.").formatted(Formatting.RED));
        }

        source.sendMessage(Text.literal("Showing available Leaderboards Page " + (page > 0 ? page : 1) + "/" + pageCount).formatted(Formatting.GOLD));

        SimpleGui gui = new SimpleGuiBuilder(ScreenHandlerType.GENERIC_9X6, false).build(source.getPlayer());
        gui.setTitle(Text.literal("Available Leaderboards Page " + (page > 0 ? page : 1)));
        gui.setLockPlayerInventory(true);

        for(int i = startItemIndex; i < Registries.CUSTOM_STAT.size() && (i - startItemIndex) < 54; i++) {
            ItemStack itemToDisplay = MOD_CONFIG.getDisplayedItem().containsKey(Registries.CUSTOM_STAT.get(i)) ? Registries.ITEM.get(MOD_CONFIG.getDisplayedItem().get(Registries.CUSTOM_STAT.get(i))).getDefaultStack() : Registries.ITEM.get(MOD_CONFIG.getDefaultDisplayedItem()).getDefaultStack();
            itemToDisplay.setCustomName(Text.translatable(Registries.CUSTOM_STAT.get(i).toTranslationKey("stat")));
            int currentIndex = i;
            gui.setSlot(currentIndex % 54, itemToDisplay, (index, type, action, gui1) -> {
                if(index == currentIndex % 54 && type.isLeft) {
                    Stat<Identifier> correspondingStat = StatService.findStatForIdentifier(Registries.CUSTOM_STAT.get(currentIndex));

                    if(correspondingStat == null) {
                        source.sendMessage(Text.literal("No Stat found for the selected identifier.").formatted(Formatting.RED));
                    } else {
                        gui.close();

                        BookGui bookGui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBook(LeaderboardDataBuilder.buildLeaderboardData(source, correspondingStat), correspondingStat));
                        bookGui.open();
                    }
                }
            });
        }

        gui.open();

        return 0;
    }
}
