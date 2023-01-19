package dev.newception.playerStatsLeaderboards.io;

import com.google.gson.*;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;
import dev.newception.playerStatsLeaderboards.config.Config;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileIO {

    static final String CONFIG_PATH = "./mods/PlayerStatsLeaderboards";

    public static void persistConfig(Config config) {
        try {
            Path dirPath = Paths.get(CONFIG_PATH);
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
                PlayerStatsLeaderboardsMod.LOGGER.debug("Created PlayerModsLeaderboards Mod folder");
            }

            Writer writer = new FileWriter(CONFIG_PATH + "/" + "config.json");

            JsonObject toSerialize = new JsonObject();
            toSerialize.addProperty("defaultDisplayedItem", config.getDefaultDisplayedItem().getNamespace() + ":" + config.getDefaultDisplayedItem().getPath());

            JsonObject displayItemSerialize = new JsonObject();
            config.getDisplayedItem().keySet().forEach(key -> {
                Identifier value = config.getDisplayedItem().get(key);
                displayItemSerialize.addProperty(key.getNamespace() + ":" + key.getPath(), value.getNamespace() + ":" + value.getPath());
            });
            toSerialize.add("displayedItem", displayItemSerialize);

            JsonObject customTranslationSerialize = new JsonObject();
            config.getCustomTranslation().keySet().forEach(key -> {
                customTranslationSerialize.addProperty(key, config.getCustomTranslation().get(key));
            });
            toSerialize.add("customTranslation", customTranslationSerialize);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(toSerialize, writer);
            writer.close();
            PlayerStatsLeaderboardsMod.LOGGER.debug("Persisted mod config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config readConfig() {
        File modConfigDirectory = new File(CONFIG_PATH);

        if(!modConfigDirectory.exists() || !modConfigDirectory.isDirectory()) {
            PlayerStatsLeaderboardsMod.LOGGER.debug("PlayerStatsLeaderboards mod folder does not exist yet");
            return null;
        }

        File configFile = new File(CONFIG_PATH + "/" + "config.json");

        if(!configFile.exists() || !configFile.isFile()) {
            PlayerStatsLeaderboardsMod.LOGGER.debug("PlayerStatsLeaderboards config file does not exist yet");
            return null;
        }

        try {
            String content = Files.readString(configFile.toPath());
            JsonElement jsonContent = JsonParser.parseString(content);

            JsonObject root = jsonContent.getAsJsonObject();
            String defaultDisplayedItem = root.get("defaultDisplayedItem").getAsString();

            JsonObject displayedItemObject = root.getAsJsonObject("displayedItem");
            Map<String, String> displayedItem = new HashMap<>();

            for(String key : displayedItemObject.keySet()) {
                String value = displayedItemObject.get(key).getAsString();
                displayedItem.put(key, value);
            }

            JsonObject customTranslationObject = root.getAsJsonObject("customTranslation");
            Map<String, String> customTranslation = new HashMap<>();

            for(String key : customTranslationObject.keySet()) {
                String value = customTranslationObject.get(key).getAsString();
                customTranslation.put(key, value);
            }

            return new Config(defaultDisplayedItem, displayedItem, customTranslation);
        } catch (Exception e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("Error while parsing Config File: " + e.getMessage());
            return null;
        }
    }
}
