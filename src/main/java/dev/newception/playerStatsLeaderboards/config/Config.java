package dev.newception.playerStatsLeaderboards.config;

import com.google.gson.*;
import dev.newception.playerStatsLeaderboards.PlayerStatsLeaderboardsMod;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Config {

    static final String CONFIG_PATH = "./mods/PlayerStatsLeaderboards";

    Identifier defaultDisplayedItem = Registries.ITEM.getId(Items.GRASS_BLOCK);
    
    final Map<Identifier, Identifier> displayedItem;
    
    final Map<Identifier, String> customTranslation;

    public Config(String defaultDisplayedItem, Map<String, String> displayedItem, Map<String, String> customTranslation) {
        Optional<Identifier> defaultDisplayedItemIdentifier = getIdentifierForString(defaultDisplayedItem, false);
        defaultDisplayedItemIdentifier.ifPresent(identifier -> this.defaultDisplayedItem = identifier);

        Map<Identifier, Identifier> displayedItemMap = new HashMap<>();
        displayedItem.keySet().stream().map(key -> getIdentifierForString(key, true)).filter(Optional::isPresent).map(Optional::get).forEach(keyIdentifier -> {
            String correspondingItem = displayedItem.get(keyIdentifier.getPath());

            if(correspondingItem == null) {
                correspondingItem = displayedItem.get(keyIdentifier.getNamespace() + ":" + keyIdentifier.getPath());
            }

            Optional<Identifier> itemIdentifier = getIdentifierForString(correspondingItem, false);

            itemIdentifier.ifPresent(identifier -> displayedItemMap.put(keyIdentifier, identifier));
        });
        this.displayedItem = displayedItemMap;

        Map<Identifier, String> customTranslationMap = new HashMap<>();
        customTranslation.keySet().stream().map(key -> getIdentifierForString(key, true)).filter(Optional::isPresent).map(Optional::get).forEach(keyIdentifier -> {
            String correspondingTranslation = customTranslation.get(keyIdentifier.getPath());

            if(correspondingTranslation == null) {
                correspondingTranslation = customTranslation.get(keyIdentifier.getNamespace() + ":" + keyIdentifier.getPath());
            }

            customTranslationMap.put(keyIdentifier, correspondingTranslation);
        });
        this.customTranslation = customTranslationMap;
    }

    protected Config(Identifier defaultDisplayedItem, Map<Identifier, Identifier> displayedItem, Map<Identifier, String> customTranslation) {
        this.defaultDisplayedItem = defaultDisplayedItem;
        this.displayedItem = displayedItem;
        this.customTranslation = customTranslation;
    }

    public static Config fromFileConfig(FileConfig fileConfig) {
        return new Config(fileConfig.getDefaultDisplayedItem(), fileConfig.getDisplayedItem(), fileConfig.getCustomTranslation());
    }

    public static Config readConfig() {
        File modConfigDirectory = new File(CONFIG_PATH);

        if(!modConfigDirectory.exists() || !modConfigDirectory.isDirectory()) {
            PlayerStatsLeaderboardsMod.LOGGER.info("PlayerStatsLeaderboards mod folder does not exist yet");
            return null;
        }

        File configFile = new File(CONFIG_PATH + "/" + "config.json");

        if(!configFile.exists() || !configFile.isFile()) {
            PlayerStatsLeaderboardsMod.LOGGER.info("PlayerStatsLeaderboards config file does not exist yet");
            return null;
        }

        try {
            String content = Files.readString(configFile.toPath());
            JsonElement jsonContent = JsonParser.parseString(content);

//            Gson gson = new GsonBuilder().create();
//            return Config.fromFileConfig(gson.fromJson(jsonContent, FileConfig.class));

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
        } catch (IOException | IllegalStateException e) {
            PlayerStatsLeaderboardsMod.LOGGER.error("Error while parsing Config File: " + e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Identifier getDefaultDisplayedItem() {
        return defaultDisplayedItem;
    }

    public Map<Identifier, Identifier> getDisplayedItem() {
        return displayedItem;
    }

    public Map<Identifier, String> getCustomTranslation() {
        return customTranslation;
    }

    private Optional<Identifier> getIdentifierForString(String s, boolean isStatIdentifier) {
        String namespace = null;
        String path = null;

        if(s.contains(":")) {
            String[] split = s.split(":");

            if(split.length != 2) {
                return Optional.empty();
            }

            namespace = split[0];
            path = split[1];
        } else {
            path = s;
        }

        return mapToIdentifierIfPossible(namespace, path, isStatIdentifier);
    }

    private Optional<Identifier> mapToIdentifierIfPossible(String namespace, String path, boolean isStatIdentifier) {
        if(isStatIdentifier) {
            return Registries.CUSTOM_STAT.stream().filter(identifier -> identifier.getPath().equals(path) && (namespace == null || identifier.getNamespace().equals(namespace))).findFirst();
        } else {
            return Registries.ITEM.stream().map(Registries.ITEM::getId).filter(identifier -> identifier.getPath().equals(path) && (namespace == null || identifier.getNamespace().equals(namespace))).findFirst();
        }
    }
}
