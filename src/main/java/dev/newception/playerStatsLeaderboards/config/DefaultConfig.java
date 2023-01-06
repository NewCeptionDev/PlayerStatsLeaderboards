package dev.newception.playerStatsLeaderboards.config;

import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

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

        displayedItem.put(Stats.ANIMALS_BRED, Registries.ITEM.getId(Items.WHEAT));
        displayedItem.put(Stats.CLEAN_ARMOR, Registries.ITEM.getId(Items.IRON_HELMET));
        displayedItem.put(Stats.CLEAN_BANNER, Registries.ITEM.getId(Items.WHITE_BANNER));
        displayedItem.put(Stats.OPEN_BARREL, Registries.ITEM.getId(Items.BARREL));
        displayedItem.put(Stats.BELL_RING, Registries.ITEM.getId(Items.BELL));
        displayedItem.put(Stats.EAT_CAKE_SLICE, Registries.ITEM.getId(Items.CAKE));
        displayedItem.put(Stats.FILL_CAULDRON, Registries.ITEM.getId(Items.CAULDRON));
        displayedItem.put(Stats.OPEN_CHEST, Registries.ITEM.getId(Items.CHEST));
        displayedItem.put(Stats.DAMAGE_ABSORBED, Registries.ITEM.getId(Items.DIAMOND_CHESTPLATE));
        displayedItem.put(Stats.DAMAGE_BLOCKED_BY_SHIELD, Registries.ITEM.getId(Items.SHIELD));
        displayedItem.put(Stats.DAMAGE_DEALT, Registries.ITEM.getId(Items.DIAMOND_SWORD));
        displayedItem.put(Stats.DAMAGE_DEALT_ABSORBED, Registries.ITEM.getId(Items.GOLDEN_APPLE));
        displayedItem.put(Stats.DAMAGE_DEALT_RESISTED, Registries.ITEM.getId(Items.GOLDEN_APPLE));
        displayedItem.put(Stats.DAMAGE_RESISTED, Registries.ITEM.getId(Items.ENCHANTED_GOLDEN_APPLE));
        displayedItem.put(Stats.DAMAGE_TAKEN, Registries.ITEM.getId(Items.APPLE));
        displayedItem.put(Stats.INSPECT_DISPENSER, Registries.ITEM.getId(Items.DISPENSER));
        displayedItem.put(Stats.CLIMB_ONE_CM, Registries.ITEM.getId(Items.LADDER));
        displayedItem.put(Stats.CROUCH_ONE_CM, Registries.ITEM.getId(Items.OAK_TRAPDOOR));
        displayedItem.put(Stats.FALL_ONE_CM, Registries.ITEM.getId(Items.HAY_BLOCK));
        displayedItem.put(Stats.FLY_ONE_CM, Registries.ITEM.getId(Items.SHULKER_BOX));
        displayedItem.put(Stats.SPRINT_ONE_CM, Registries.ITEM.getId(Items.DIRT_PATH));
        displayedItem.put(Stats.SWIM_ONE_CM, Registries.ITEM.getId(Items.WATER_BUCKET));
        displayedItem.put(Stats.WALK_ONE_CM, Registries.ITEM.getId(Items.DIRT_PATH));
        displayedItem.put(Stats.WALK_ON_WATER_ONE_CM, Registries.ITEM.getId(Items.WHEAT));
        displayedItem.put(Stats.WALK_UNDER_WATER_ONE_CM, Registries.ITEM.getId(Items.WATER_BUCKET));
        displayedItem.put(Stats.BOAT_ONE_CM, Registries.ITEM.getId(Items.OAK_BOAT));
        displayedItem.put(Stats.AVIATE_ONE_CM, Registries.ITEM.getId(Items.ELYTRA));
        displayedItem.put(Stats.HORSE_ONE_CM, Registries.ITEM.getId(Items.IRON_HORSE_ARMOR));
        displayedItem.put(Stats.MINECART_ONE_CM, Registries.ITEM.getId(Items.MINECART));
        displayedItem.put(Stats.PIG_ONE_CM, Registries.ITEM.getId(Items.CARROT_ON_A_STICK));
        displayedItem.put(Stats.STRIDER_ONE_CM, Registries.ITEM.getId(Items.CARROT_ON_A_STICK));
        displayedItem.put(Stats.INSPECT_DROPPER, Registries.ITEM.getId(Items.DROPPER));
        displayedItem.put(Stats.OPEN_ENDERCHEST, Registries.ITEM.getId(Items.ENDER_CHEST));
        displayedItem.put(Stats.FISH_CAUGHT, Registries.ITEM.getId(Items.FISHING_ROD));
        displayedItem.put(Stats.LEAVE_GAME, Registries.ITEM.getId(Items.IRON_DOOR));
        displayedItem.put(Stats.INSPECT_HOPPER, Registries.ITEM.getId(Items.HOPPER));
        displayedItem.put(Stats.INTERACT_WITH_ANVIL, Registries.ITEM.getId(Items.ANVIL));
        displayedItem.put(Stats.INTERACT_WITH_BEACON, Registries.ITEM.getId(Items.BEACON));
        displayedItem.put(Stats.INTERACT_WITH_BLAST_FURNACE, Registries.ITEM.getId(Items.BLAST_FURNACE));
        displayedItem.put(Stats.INTERACT_WITH_BREWINGSTAND, Registries.ITEM.getId(Items.BREWING_STAND));
        displayedItem.put(Stats.INTERACT_WITH_CAMPFIRE, Registries.ITEM.getId(Items.CAMPFIRE));
        displayedItem.put(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE, Registries.ITEM.getId(Items.CARTOGRAPHY_TABLE));
        displayedItem.put(Stats.INTERACT_WITH_CRAFTING_TABLE, Registries.ITEM.getId(Items.CRAFTING_TABLE));
        displayedItem.put(Stats.INTERACT_WITH_FURNACE, Registries.ITEM.getId(Items.FURNACE));
        displayedItem.put(Stats.INTERACT_WITH_GRINDSTONE, Registries.ITEM.getId(Items.GRINDSTONE));
        displayedItem.put(Stats.INTERACT_WITH_LECTERN, Registries.ITEM.getId(Items.LECTERN));
        displayedItem.put(Stats.INTERACT_WITH_LOOM, Registries.ITEM.getId(Items.LOOM));
        displayedItem.put(Stats.INTERACT_WITH_SMITHING_TABLE, Registries.ITEM.getId(Items.SMITHING_TABLE));
        displayedItem.put(Stats.INTERACT_WITH_SMOKER, Registries.ITEM.getId(Items.SMOKER));
        displayedItem.put(Stats.INTERACT_WITH_STONECUTTER, Registries.ITEM.getId(Items.STONECUTTER));
        displayedItem.put(Stats.DROP, Registries.ITEM.getId(Items.DROPPER));
        displayedItem.put(Stats.ENCHANT_ITEM, Registries.ITEM.getId(Items.ENCHANTING_TABLE));
        displayedItem.put(Stats.JUMP, Registries.ITEM.getId(Items.LEATHER_BOOTS));
        displayedItem.put(Stats.MOB_KILLS, Registries.ITEM.getId(Items.ZOMBIE_HEAD));
        displayedItem.put(Stats.PLAY_RECORD, Registries.ITEM.getId(Items.MUSIC_DISC_13));
        displayedItem.put(Stats.PLAY_NOTEBLOCK, Registries.ITEM.getId(Items.NOTE_BLOCK));
        displayedItem.put(Stats.TUNE_NOTEBLOCK, Registries.ITEM.getId(Items.NOTE_BLOCK));
        displayedItem.put(Stats.DEATHS, Registries.ITEM.getId(Items.TOTEM_OF_UNDYING));
        displayedItem.put(Stats.POT_FLOWER, Registries.ITEM.getId(Items.FLOWER_POT));
        displayedItem.put(Stats.PLAYER_KILLS, Registries.ITEM.getId(Items.PLAYER_HEAD));
        displayedItem.put(Stats.RAID_TRIGGER, Registries.ITEM.getId(Items.CROSSBOW));
        displayedItem.put(Stats.RAID_WIN, Registries.ITEM.getId(Items.EMERALD));
        displayedItem.put(Stats.CLEAN_SHULKER_BOX, Registries.ITEM.getId(Items.SHULKER_BOX));
        displayedItem.put(Stats.OPEN_SHULKER_BOX, Registries.ITEM.getId(Items.SHULKER_BOX));
        displayedItem.put(Stats.SNEAK_TIME, Registries.ITEM.getId(Items.SCULK_SHRIEKER));
        displayedItem.put(Stats.TALKED_TO_VILLAGER, Registries.ITEM.getId(Items.EMERALD));
        displayedItem.put(Stats.TARGET_HIT, Registries.ITEM.getId(Items.TARGET));
        displayedItem.put(Stats.PLAY_TIME, Registries.ITEM.getId(Items.CLOCK));
        displayedItem.put(Stats.TIME_SINCE_DEATH, Registries.ITEM.getId(Items.CLOCK));
        displayedItem.put(Stats.TIME_SINCE_REST, Registries.ITEM.getId(Items.CLOCK));
        displayedItem.put(Stats.TOTAL_WORLD_TIME, Registries.ITEM.getId(Items.CLOCK));
        displayedItem.put(Stats.SLEEP_IN_BED, Registries.ITEM.getId(Items.RED_BED));
        displayedItem.put(Stats.TRADED_WITH_VILLAGER, Registries.ITEM.getId(Items.EMERALD));
        displayedItem.put(Stats.TRIGGER_TRAPPED_CHEST, Registries.ITEM.getId(Items.TRAPPED_CHEST));
        displayedItem.put(Stats.USE_CAULDRON, Registries.ITEM.getId(Items.CAULDRON));

        return displayedItem;
    }

    private static Map<String, String> getDefaultConfigCustomTranslation() {
        return new HashMap<>();
    }
}
