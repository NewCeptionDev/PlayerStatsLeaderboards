package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import eu.pb4.sgui.api.gui.BookGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
}
