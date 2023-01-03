package dev.newception.playerStatsLeaderboards.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class UsernameFileReader {

    public static String readDisplayNameOfPlayer(UUID playerUUID) {
        File usercacheFile = new File("./usercache.json");

        try {
            String userCacheContent = Files.readString(usercacheFile.toPath());
            JsonArray json = JsonParser.parseString(userCacheContent).getAsJsonArray();
            for(JsonElement elem : json.asList()) {
                if(elem.getAsJsonObject().get("uuid").getAsString().equals(playerUUID.toString())) {
                    String username = elem.getAsJsonObject().get("name").getAsString();

                    return username;
                }
            }

        } catch (IOException e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("There was an IO Error while trying to read the usercache");
            PlayerStatsLeaderboardsMod.LOGGER.error("Error Message is: " + e.getMessage());
            return null;
        }

        return null;
    }

}
