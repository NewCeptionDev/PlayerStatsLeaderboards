package dev.newception.playerStatsLeaderboards.config;

import java.util.Map;

public class FileConfig {

    private String defaultDisplayedItem;

    private Map<String, String> displayedItem;

    private Map<String, String> customTranslation;

    public FileConfig(String defaultDisplayedItem, Map<String, String> displayedItem, Map<String, String> customTranslation) {
        this.defaultDisplayedItem = defaultDisplayedItem;
        this.displayedItem = displayedItem;
        this.customTranslation = customTranslation;
    }

    public String getDefaultDisplayedItem() {
        return defaultDisplayedItem;
    }

    public Map<String, String> getDisplayedItem() {
        return displayedItem;
    }

    public Map<String, String> getCustomTranslation() {
        return customTranslation;
    }
}
