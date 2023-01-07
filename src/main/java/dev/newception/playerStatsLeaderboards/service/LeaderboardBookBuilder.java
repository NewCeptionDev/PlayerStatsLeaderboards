package dev.newception.playerStatsLeaderboards.service;

import dev.newception.playerStatsLeaderboards.config.StatisticTranslation;
import dev.newception.playerStatsLeaderboards.util.ItemStatsType;
import dev.newception.playerStatsLeaderboards.util.MobStatsType;
import dev.newception.playerStatsLeaderboards.util.PlayerStatValue;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod.MOD_CONFIG;

public class LeaderboardBookBuilder {

    private static final int LEADERBOARD_LINES_PER_PAGE = 10;
    private static final int LEADERBOARD_LINES_FIRST_PAGE = 8;

    public static ItemStack buildWrittenBookForGeneralStatistic(List<PlayerStatValue> playerStatValues, Stat<Identifier> stat) {
        NbtList bookContent = new NbtList();
        List<String> pages = buildStringPages(playerStatValues, stat, stat.getValue().toTranslationKey("stat"), null);

        for(String page : pages) {
            bookContent.add(NbtString.of(page));
        }

        return buildWrittenBook(bookContent);
    }

    public static ItemStack buildWrittenBookForItemStatistic(Map<ItemStatsType, List<PlayerStatValue>> playerStatValues, Item item) {
        NbtList bookContent = new NbtList();

        for(ItemStatsType key : playerStatValues.keySet()) {
            buildPagesFromData(bookContent, playerStatValues.get(key), key.toStatType().getOrCreateStat(item), key.getIdentifier(), item.getName());
        }

        return buildWrittenBook(bookContent);
    }

    public static ItemStack buildWrittenBookForMobStatistic(Map<MobStatsType, List<PlayerStatValue>> playerStatValues, EntityType entityType) {
        NbtList bookContent = new NbtList();

        for(MobStatsType key : playerStatValues.keySet()) {
            buildPagesFromData(bookContent, playerStatValues.get(key), key.toStatType().getOrCreateStat(entityType), key.getIdentifier(), entityType.getName());
        }

        return buildWrittenBook(bookContent);
    }

    private static <T> void buildPagesFromData(NbtList bookContent, List<PlayerStatValue> playerStatValues, Stat<T> stat, String identifier, Text name) {
        if(playerStatValues.stream().anyMatch(playerStatValue -> playerStatValue.statValue() > 0)) {
            List<String> pages = buildStringPages(playerStatValues, stat, "stat_type." + identifier.replaceAll(":", "."), name.getString());

            for(String page : pages) {
                bookContent.add(NbtString.of(page));
            }
        }
    }

    private static <T> List<String> buildStringPages(List<PlayerStatValue> playerStatValues, Stat<T> stat, String translationIdentifier, String nameSuffix) {
        List<String> pagesAsString = new ArrayList<>();
        int pages = getPagesNeeded(playerStatValues.size());

        for (int i = 0; i < pages; i++) {
            StringBuilder pageContentBuilder = new StringBuilder();
            int playersToList = LEADERBOARD_LINES_PER_PAGE;
            if (i == 0) {
                pageContentBuilder.append("Leaderboard for ").append(getTranslationForStatistic(translationIdentifier));
                if(nameSuffix != null) {
                    pageContentBuilder.append(" ").append(nameSuffix);
                }

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
            pagesAsString.add(pageContentBuilder.toString());
        }

        return pagesAsString;
    }

    private static ItemStack buildWrittenBook(NbtList pages) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tags = new NbtCompound();
        tags.putString("title", "Leaderboard");
        tags.putString("author", "PlayerStatsLeaderboards");

        if(pages.isEmpty()) {
            pages.add(NbtString.of("No comparable Statistics to show.\n\nNo Player has done any action that increases this statistic."));
        }

        tags.put("pages", pages);
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

    private static String getTranslationForStatistic(String identifier) {
        if(MOD_CONFIG.getCustomTranslation().containsKey(identifier)) {
            return MOD_CONFIG.getCustomTranslation().get(identifier);
        }

        if(StatisticTranslation.getTRANSLATION().containsKey(identifier)) {
            return StatisticTranslation.getTRANSLATION().get(identifier);
        }

        return identifier;
    }
}
