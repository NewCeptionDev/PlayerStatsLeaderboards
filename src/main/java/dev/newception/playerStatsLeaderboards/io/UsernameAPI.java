package dev.newception.playerStatsLeaderboards.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class UsernameAPI {

    private static final String BASE_URL = "https://playerdb.co/api/player/minecraft/";

    // TODO make sure to only call API when Server is in online mode (otherwise the call is useless as the uuid will be incorrect)

    public static String getUsernameForUUID(UUID playerUUID) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + playerUUID.toString()))
                    .setHeader("user-agent", "PlayerStatsLeaderboards Mod for Minecraft")
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();

            if(responseJson.get("code").getAsString().equals("player.found")) {
                return responseJson.getAsJsonObject("data").getAsJsonObject("player").get("username").getAsString();
            } else {
                PlayerStatsLeaderboardsMod.LOGGER.error("API Call returned no player for the given UUID " + playerUUID);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("API Call finished with an error for UUID " + playerUUID);
            PlayerStatsLeaderboardsMod.LOGGER.error("Error Message is: " + e.getMessage());
            return null;
        }
    }
}
