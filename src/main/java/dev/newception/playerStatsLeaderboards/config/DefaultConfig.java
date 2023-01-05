package dev.newception.playerStatsLeaderboards.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DefaultConfig extends Config {

    public DefaultConfig() {
        super(getDefaultConfigDefaultDisplayedItem(), getDefaultConfigDisplayedItem(), getDefaultConfigCustomTranslation());
    }

    private static Identifier getDefaultConfigDefaultDisplayedItem() {
        return Registries.ITEM.getId(Items.GRASS_BLOCK);
    }

    private static Map<Identifier, Identifier> getDefaultConfigDisplayedItem() {
        Map<Identifier, Identifier> displayedItem = new HashMap<>();

        displayedItem.put(Stats.PLAY_TIME, Registries.ITEM.getId(Items.CLOCK));

        return displayedItem;
    }

    private static Map<Identifier, String> getDefaultConfigCustomTranslation() {
        return new HashMap<>();
    }

    public static void persistConfig(Config config) {
        try {
            Path dirPath = Paths.get(CONFIG_PATH);
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
                PlayerStatsLeaderboardsMod.LOGGER.info("Created PlayerModsLeaderboards Mod folder");
            }

            Writer writer = new FileWriter(CONFIG_PATH + "/" + "config.json");

            PlayerStatsLeaderboardsMod.LOGGER.info(config.getDefaultDisplayedItem().toString());

            JsonObject toSerialize = new JsonObject();
            toSerialize.addProperty("defaultDisplayedItem", config.getDefaultDisplayedItem().getPath());

            JsonObject displayItemSerialize = new JsonObject();
            config.displayedItem.keySet().forEach(key -> {
                displayItemSerialize.addProperty(key.getPath(), config.displayedItem.get(key).getPath());
            });
            toSerialize.add("displayedItem", displayItemSerialize);

            JsonObject customTranslationSerialize = new JsonObject();
            config.customTranslation.keySet().forEach(key -> {
                customTranslationSerialize.addProperty(key.getPath(), config.customTranslation.get(key));
            });
            toSerialize.add("customTranslation", customTranslationSerialize);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(toSerialize, writer);
            writer.close();
            PlayerStatsLeaderboardsMod.LOGGER.info("Persisted mod config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
