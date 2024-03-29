package dev.newception.playerStatsLeaderboards.config;

import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Config {

    Identifier defaultDisplayedItem = Registries.ITEM.getId(Items.GRASS_BLOCK);
    
    final Map<Identifier, Identifier> displayedItem;
    
    final Map<String, String> customTranslation;

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

        Map<String, String> customTranslationMap = new HashMap<>();
        customTranslation.keySet().forEach(key -> {
            String correspondingTranslation = customTranslation.get(key);

            customTranslationMap.put(key, correspondingTranslation);
        });
        this.customTranslation = customTranslationMap;
    }

    protected Config(Identifier defaultDisplayedItem, Map<Identifier, Identifier> displayedItem, Map<String, String> customTranslation) {
        this.defaultDisplayedItem = defaultDisplayedItem;
        this.displayedItem = displayedItem;
        this.customTranslation = customTranslation;
    }

    public Identifier getDefaultDisplayedItem() {
        return defaultDisplayedItem;
    }

    public Map<Identifier, Identifier> getDisplayedItem() {
        return displayedItem;
    }

    public Map<String, String> getCustomTranslation() {
        return customTranslation;
    }

    private Optional<Identifier> getIdentifierForString(String s, boolean isStatIdentifier) {
        String namespace = null;
        String path;

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
