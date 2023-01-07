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

public class LeaderboardSelectionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("leaderboards")
                .then(literal("show")
                        .executes(context -> getAvailableGeneralStatisticLeaderboardsGUI(context.getSource(), 0))));

        dispatcher.register(literal("leaderboards")
                .then(literal("show")
                        .then(argument("page", IntegerArgumentType.integer(1))
                        .executes(context -> getAvailableGeneralStatisticLeaderboardsGUI(context.getSource(), IntegerArgumentType.getInteger(context, "page"))))));
    }

    public static int getAvailableGeneralStatisticLeaderboardsGUI(ServerCommandSource source, int page) {
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

                        BookGui bookGui = new BookGui(source.getPlayer(), LeaderboardBookBuilder.buildWrittenBookForGeneralStatistic(LeaderboardDataBuilder.buildLeaderboardDataGeneralStatistic(source, correspondingStat), correspondingStat));
                        bookGui.open();
                    }
                }
            });
        }

        gui.open();

        return 0;
    }
}
