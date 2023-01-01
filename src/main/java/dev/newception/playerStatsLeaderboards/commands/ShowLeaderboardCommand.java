package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShowLeaderboardCommand {

    private static final int LEADERBOARD_LINES_PER_PAGE = 10;
    private static final int LEADERBOARD_LINES_FIRST_PAGE = 8;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("leaderboards")
                .then(argument("stat", IdentifierArgumentType.identifier())
                        .executes(context -> showLeaderboard(context.getSource(), IdentifierArgumentType.getIdentifier(context, "stat")))));

    }

    private static int showLeaderboard(ServerCommandSource source, Identifier statIdentifier) {
        if(!Registries.CUSTOM_STAT.containsId(statIdentifier)) {
            source.sendMessage(Text.literal("§4The given identifier is not a statistic."));
            return 0;
        }

        List<PlayerStatValue> playerStatValueList = new ArrayList<>();

        Stat<Identifier> leaderboardStat = null;

        for(Stat<Identifier> stat : Stats.CUSTOM) {
            if(stat.getValue().getPath().equals(statIdentifier.getPath())) {
                leaderboardStat = stat;
                break;
            }
        }

        if(leaderboardStat == null) {
            source.sendMessage(Text.literal("§4No Stat found for the given identifier."));
            return -1;
        }

        PlayerStatsLeaderboardsMod.LOGGER.info("After getting Stat with: " + leaderboardStat);

        Stat<Identifier> finalLeaderboardStat = leaderboardStat;
        source.getServer().getPlayerManager().getPlayerList().forEach(player -> {
            PlayerStatsLeaderboardsMod.LOGGER.info("Adding Values for player: " + player);
            int statValue = player.getStatHandler().getStat(finalLeaderboardStat);
            PlayerStatsLeaderboardsMod.LOGGER.info("After StatValue: " + statValue);
            String playerName = player.getDisplayName().getString();
            PlayerStatsLeaderboardsMod.LOGGER.info("After playerName: " + playerName);

            playerStatValueList.add(new PlayerStatValue(playerName, statValue));
        });

        PlayerStatsLeaderboardsMod.LOGGER.info("Added all Players");

        playerStatValueList.sort(PlayerStatValue::compareTo);

        ItemStack leaderboardBook = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tags = new NbtCompound();
        tags.putString("title", Text.translatable(statIdentifier.toTranslationKey("stat")) + " Leaderboard");
        tags.putString("author", "PlayerStatsLeaderboards");

        NbtList bookContent = new NbtList();
        int pages = getPagesNeeded(playerStatValueList.size());

        PlayerStatsLeaderboardsMod.LOGGER.info("Writing Book");

        for(int i = 0; i < pages; i++) {
            StringBuilder pageContentBuilder = new StringBuilder();
            int playersToList = LEADERBOARD_LINES_PER_PAGE;
            if(i == 0) {
                pageContentBuilder.append("Leaderboard for Statistic ").append(Text.translatable(statIdentifier.toTranslationKey("stat")));
                pageContentBuilder.append("\n");
                pageContentBuilder.append("\n");

                playersToList = LEADERBOARD_LINES_FIRST_PAGE;
            }

            for(int j = 0; j < playersToList && getPosition(i, j) - 1 < playerStatValueList.size(); j++) {
                int position = getPosition(i, j);
                PlayerStatValue playerStatValue = playerStatValueList.get(position - 1);

                PlayerStatsLeaderboardsMod.LOGGER.info("Position: " + position);
                PlayerStatsLeaderboardsMod.LOGGER.info("StatValue: " + playerStatValue);

                pageContentBuilder.append(position);
                pageContentBuilder.append(". ");
                pageContentBuilder.append(playerStatValue.playerName);
                pageContentBuilder.append(" (");
                pageContentBuilder.append(playerStatValue.statValue);
                pageContentBuilder.append(")");
                pageContentBuilder.append("\n");
            }
            PlayerStatsLeaderboardsMod.LOGGER.info("Final String: " + pageContentBuilder.toString());

            bookContent.add(NbtString.of(pageContentBuilder.toString()));
        }

        PlayerStatsLeaderboardsMod.LOGGER.info("After writing Content");
        
        tags.put("pages", bookContent);
        leaderboardBook.setNbt(tags);

        PlayerStatsLeaderboardsMod.LOGGER.info("After setting Tags");

        BookGui gui = new BookGui(source.getPlayer(), leaderboardBook);

        gui.open();

        return 0;
    }

    private static int getPagesNeeded(int players) {
        int pagesNeeded = 1;

        if(players - LEADERBOARD_LINES_FIRST_PAGE > 0) {
            pagesNeeded += Math.floorDiv(players - LEADERBOARD_LINES_FIRST_PAGE, LEADERBOARD_LINES_PER_PAGE) + 1;
        }

        return pagesNeeded;
    }

    private static int getPosition(int page, int positionOnPage) {
        if(page == 0) {
            return positionOnPage + 1;
        }

        return LEADERBOARD_LINES_FIRST_PAGE + (page - 1) * LEADERBOARD_LINES_PER_PAGE + positionOnPage + 1;
    }

    static class PlayerStatValue implements Comparable<PlayerStatValue>{
        private final String playerName;
        private final int statValue;

        public PlayerStatValue(String playerName, int statValue) {
            this.playerName = playerName;
            this.statValue = statValue;
        }

        @Override
        public int compareTo(@NotNull PlayerStatValue o) {
            return Integer.compare(statValue, o.statValue);
        }
    }
}
