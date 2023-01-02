package dev.newception.playerStatsLeaderboards.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class UsernameFileReader {

    public static String readDisplayNameOfPlayer(UUID player) {
        File usercacheFile = new File("./usercache.json");

        try {
            String userCacheContent = Files.readString(usercacheFile.toPath());
            JsonArray json = JsonParser.parseString(userCacheContent).getAsJsonArray();
            for(JsonElement elem : json.asList()) {
                if(elem.getAsJsonObject().get("uuid").getAsString().equals(player.toString())) {
                    return elem.getAsJsonObject().get("name").getAsString();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Player was not found");
    }

}
