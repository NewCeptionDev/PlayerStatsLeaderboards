package dev.newception.playerStatsLeaderboards.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class StatsFileReader {

    public static int readStatForPlayer(UUID player, String stat, Path statsPath) {
        try {
            String statsContent = Files.readString(statsPath.resolve(player.toString() + ".json"));
            JsonObject jsonRoot = JsonParser.parseString(statsContent).getAsJsonObject();

            PlayerStatsLeaderboardsMod.LOGGER.info("StatsContent: " + statsContent);

            JsonObject statsObject = jsonRoot.getAsJsonObject("stats");
            JsonObject customStats = statsObject.getAsJsonObject("minecraft:custom");

            if(customStats.has("minecraft:" + stat)) {
                return customStats.get("minecraft:" + stat).getAsInt();
            }
            return 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
