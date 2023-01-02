package dev.newception.playerStatsLeaderboards.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.newception.playerStatsLeaderboards.util.StatsFileReader;
import dev.newception.playerStatsLeaderboards.util.UsernameFileReader;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.REGISTERED_PLAYERS;
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
        REGISTERED_PLAYERS.stream().forEach(uuid -> {
            String playerName;
            int statValue;
            if (source.getServer().getPlayerManager().getPlayer(uuid) != null) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(uuid);

                playerName = player.getDisplayName().getString();
                statValue = player.getStatHandler().getStat(finalLeaderboardStat);
            } else {
                playerName = UsernameFileReader.readDisplayNameOfPlayer(uuid);
                statValue = StatsFileReader.readStatForPlayer(uuid, finalLeaderboardStat.getValue().getPath(), source.getServer().getSavePath(WorldSavePath.STATS));
            }
            playerStatValueList.add(new PlayerStatValue(playerName, statValue));
        });

        playerStatValueList.sort(Comparator.reverseOrder());

        ItemStack leaderboardBook = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tags = new NbtCompound();
        tags.putString("title", leaderboardStat.getValue().

                getPath() + " Leaderboard");
        tags.putString("author", "PlayerStatsLeaderboards");

        NbtList bookContent = new NbtList();
        int pages = getPagesNeeded(playerStatValueList.size());

        for (
                int i = 0;
                i < pages; i++) {
            StringBuilder pageContentBuilder = new StringBuilder();
            int playersToList = LEADERBOARD_LINES_PER_PAGE;
            if (i == 0) {
                pageContentBuilder.append("Leaderboard for Statistic ").append(leaderboardStat.getValue().getPath());
                pageContentBuilder.append("\n");
                pageContentBuilder.append("\n");

                playersToList = LEADERBOARD_LINES_FIRST_PAGE;
            }

            for (int j = 0; j < playersToList && getPosition(i, j) - 1 < playerStatValueList.size(); j++) {
                int position = getPosition(i, j);
                PlayerStatValue playerStatValue = playerStatValueList.get(position - 1);

                pageContentBuilder.append(position);
                pageContentBuilder.append(". ");
                pageContentBuilder.append(playerStatValue.playerName);
                pageContentBuilder.append(" (");
                pageContentBuilder.append(leaderboardStat.format(playerStatValue.statValue));
                pageContentBuilder.append(")");
                pageContentBuilder.append("\n");
            }
            bookContent.add(NbtString.of(pageContentBuilder.toString()));
        }

        tags.put("pages", bookContent);
        leaderboardBook.setNbt(tags);

        BookGui gui = new BookGui(source.getPlayer(), leaderboardBook);

        gui.open();

        return 0;
    }

    private static int getPagesNeeded(int players) {
        int pagesNeeded = 1;

        if (players - LEADERBOARD_LINES_FIRST_PAGE > 0) {
            pagesNeeded += Math.floorDiv(players - LEADERBOARD_LINES_FIRST_PAGE, LEADERBOARD_LINES_PER_PAGE) + 1;
        }

        return pagesNeeded;
    }

    private static int getPosition(int page, int positionOnPage) {
        if (page == 0) {
            return positionOnPage + 1;
        }

        return LEADERBOARD_LINES_FIRST_PAGE + (page - 1) * LEADERBOARD_LINES_PER_PAGE + positionOnPage + 1;
    }

    static class PlayerStatValue implements Comparable<PlayerStatValue> {
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
