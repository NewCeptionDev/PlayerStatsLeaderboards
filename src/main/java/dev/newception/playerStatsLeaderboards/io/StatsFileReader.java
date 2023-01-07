package dev.newception.playerStatsLeaderboards.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatsFileReader {

    public static Integer readGeneralStatForPlayer(UUID player, String stat, Path statsPath) {
        try {
            PlayerStatsLeaderboardsMod.LOGGER.info("Reading Stat File of Player " + player + " for GeneralStat");
            String statsContent = Files.readString(statsPath.resolve(player.toString() + ".json"));
            JsonObject jsonRoot = JsonParser.parseString(statsContent).getAsJsonObject();

            JsonObject statsObject = jsonRoot.getAsJsonObject("stats");
            JsonObject customStats = statsObject.getAsJsonObject("minecraft:custom");

            if(customStats.has(stat)) {
                PlayerStatsLeaderboardsMod.LOGGER.info("Loaded GeneralStat for Player " + player + " from File");
                return customStats.get(stat).getAsInt();
            }
            return 0;
        } catch (IOException e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("There was an IO Error while trying to read the stats for UUID " + player);
            PlayerStatsLeaderboardsMod.LOGGER.error("Error Message is: " + e.getMessage());
            return null;
        }
    }

    public static Integer readNonGeneralStatForPlayer(UUID player, String statType, String stat, Path statsPath) {
        try {
            PlayerStatsLeaderboardsMod.LOGGER.info("Reading Stat File of Player " + player + " for " + statType);
            String statsContent = Files.readString(statsPath.resolve(player.toString() + ".json"));
            JsonObject jsonRoot = JsonParser.parseString(statsContent).getAsJsonObject();

            JsonObject statsObject = jsonRoot.getAsJsonObject("stats");
            if(!statsObject.has(statType)) {
                return 0;
            }

            JsonObject statsForStatType = statsObject.getAsJsonObject(statType);
            if(statsForStatType.has(stat)) {
                PlayerStatsLeaderboardsMod.LOGGER.info("Loaded " + statType + " Stat for Player " + player + " from File");
                return statsForStatType.get(stat).getAsInt();
            }
            return 0;
        } catch (IOException e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("There was an IO Error while trying to read the stats for UUID " + player);
            PlayerStatsLeaderboardsMod.LOGGER.error("Error Message is: " + e.getMessage());
            return null;
        }
    }

    public static Set<UUID> getUUIDsFromAllPlayersEveryJoined() {
        String path = "./world/stats/";

        if(!new File(path).exists()) {
            return new HashSet<>();
        }

        return Stream.of(Objects.requireNonNull(new File(path).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .filter(s -> !s.isEmpty())
                .map(s -> s.split("\\.")[0])
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }

}
