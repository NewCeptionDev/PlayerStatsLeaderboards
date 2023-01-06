package dev.newception.playerStatsLeaderboards.service;

import dev.newception.playerStatsLeaderboards.config.StatisticTranslation;
import dev.newception.playerStatsLeaderboards.util.PlayerStatValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;

import java.util.List;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.MOD_CONFIG;

public class LeaderboardBookBuilder {

    private static final int LEADERBOARD_LINES_PER_PAGE = 10;
    private static final int LEADERBOARD_LINES_FIRST_PAGE = 8;

    // TODO Use BookElementBuilder

    public static ItemStack buildWrittenBook(List<PlayerStatValue> playerStatValues, Stat<Identifier> stat) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tags = new NbtCompound();
        tags.putString("title", "Leaderboard");
        tags.putString("author", "PlayerStatsLeaderboards");

        NbtList bookContent = new NbtList();
        int pages = getPagesNeeded(playerStatValues.size());

        for (int i = 0; i < pages; i++) {
            StringBuilder pageContentBuilder = new StringBuilder();
            int playersToList = LEADERBOARD_LINES_PER_PAGE;
            if (i == 0) {
                pageContentBuilder.append("Leaderboard for ").append(getTranslationForStatistic(stat));
                pageContentBuilder.append("\n");
                pageContentBuilder.append("\n");

                playersToList = LEADERBOARD_LINES_FIRST_PAGE;
            }

            for (int j = 0; j < playersToList && getPosition(i, j) - 1 < playerStatValues.size(); j++) {
                int position = getPosition(i, j);
                PlayerStatValue playerStatValue = playerStatValues.get(position - 1);

                pageContentBuilder.append(position);
                pageContentBuilder.append(". ");
                pageContentBuilder.append(playerStatValue.playerName());
                pageContentBuilder.append(" (");
                pageContentBuilder.append(stat.format(playerStatValue.statValue()));
                pageContentBuilder.append(")");
                pageContentBuilder.append("\n");
            }
            bookContent.add(NbtString.of(pageContentBuilder.toString()));
        }

        tags.put("pages", bookContent);
        book.setNbt(tags);

        return book;
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

    private static String getTranslationForStatistic(Stat<Identifier> statistic) {
        if(MOD_CONFIG.getCustomTranslation().containsKey(statistic.getValue().toTranslationKey("stat"))) {
            return MOD_CONFIG.getCustomTranslation().get(statistic.getValue().toTranslationKey("stat"));
        }

        if(StatisticTranslation.getTRANSLATION().containsKey(statistic.getValue().toTranslationKey("stat"))) {
            return StatisticTranslation.getTRANSLATION().get(statistic.getValue().toTranslationKey("stat"));
        }

        return statistic.getValue().toShortTranslationKey();
    }
}
