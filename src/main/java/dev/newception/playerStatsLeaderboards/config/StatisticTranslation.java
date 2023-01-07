package dev.newception.playerStatsLeaderboards.config;

import java.util.HashMap;
import java.util.Map;

public class StatisticTranslation {

    private static Map<String, String> TRANSLATION = populateTranslationMap();

    private static Map<String, String> populateTranslationMap() {
        Map<String, String> translation = new HashMap<>();

        // General Stats Translations
        translation.put("stat.minecraft.animals_bred", "Animals Bred");
        translation.put("stat.minecraft.aviate_one_cm", "Distance by Elytra");
        translation.put("stat.minecraft.bell_ring", "Bells Rung");
        translation.put("stat.minecraft.boat_one_cm", "Distance by Boat");
        translation.put("stat.minecraft.clean_armor", "Armour Pieces Cleaned");
        translation.put("stat.minecraft.clean_banner", "Banners Cleaned");
        translation.put("stat.minecraft.clean_shulker_box", "Shulker Boxes Cleaned");
        translation.put("stat.minecraft.climb_one_cm", "Distance Climbed");
        translation.put("stat.minecraft.crouch_one_cm", "Distance Crouched");
        translation.put("stat.minecraft.damage_absorbed", "Damage Absorbed");
        translation.put("stat.minecraft.damage_blocked_by_shield", "Damage Blocked by Shield");
        translation.put("stat.minecraft.damage_dealt", "Damage Dealt");
        translation.put("stat.minecraft.damage_dealt_absorbed", "Damage Dealt (Absorbed)");
        translation.put("stat.minecraft.damage_dealt_resisted", "Damage Dealt (Resisted)");
        translation.put("stat.minecraft.damage_resisted", "Damage Resisted");
        translation.put("stat.minecraft.damage_taken", "Damage Taken");
        translation.put("stat.minecraft.deaths", "Number of Deaths");
        translation.put("stat.minecraft.drop", "Items Dropped");
        translation.put("stat.minecraft.eat_cake_slice", "Cake Slices Eaten");
        translation.put("stat.minecraft.enchant_item", "Items Enchanted");
        translation.put("stat.minecraft.fall_one_cm", "Distance Fallen");
        translation.put("stat.minecraft.fill_cauldron", "Cauldrons Filled");
        translation.put("stat.minecraft.fish_caught", "Fish Caught");
        translation.put("stat.minecraft.fly_one_cm", "Distance Flown");
        translation.put("stat.minecraft.horse_one_cm", "Distance by Horse");
        translation.put("stat.minecraft.inspect_dispenser", "Dispensers Searched");
        translation.put("stat.minecraft.inspect_dropper", "Droppers Searched");
        translation.put("stat.minecraft.inspect_hopper", "Hoppers Searched");
        translation.put("stat.minecraft.interact_with_anvil", "Interactions with Anvil");
        translation.put("stat.minecraft.interact_with_beacon", "Interactions with Beacon");
        translation.put("stat.minecraft.interact_with_blast_furnace", "Interactions with Blast Furnace");
        translation.put("stat.minecraft.interact_with_brewingstand", "Interactions with Brewing Stand");
        translation.put("stat.minecraft.interact_with_campfire", "Interactions with Campfire");
        translation.put("stat.minecraft.interact_with_cartography_table", "Interactions with Cartography Table");
        translation.put("stat.minecraft.interact_with_crafting_table", "Interactions with Crafting Table");
        translation.put("stat.minecraft.interact_with_furnace", "Interactions with Furnace");
        translation.put("stat.minecraft.interact_with_grindstone", "Interactions with Grindstone");
        translation.put("stat.minecraft.interact_with_lectern", "Interactions with Lectern");
        translation.put("stat.minecraft.interact_with_loom", "Interactions with Loom");
        translation.put("stat.minecraft.interact_with_smithing_table", "Interactions with Smithing Table");
        translation.put("stat.minecraft.interact_with_smoker", "Interactions with Smoker");
        translation.put("stat.minecraft.interact_with_stonecutter", "Interactions with Stonecutter");
        translation.put("stat.minecraft.jump", "Jumps");
        translation.put("stat.minecraft.junk_fished", "Junk Fished");
        translation.put("stat.minecraft.leave_game", "Games Quit");
        translation.put("stat.minecraft.minecart_one_cm", "Distance by Minecart");
        translation.put("stat.minecraft.mob_kills", "Mob Kills");
        translation.put("stat.minecraft.open_barrel", "Barrels Opened");
        translation.put("stat.minecraft.open_chest", "Chests Opened");
        translation.put("stat.minecraft.open_enderchest", "Ender Chests Opened");
        translation.put("stat.minecraft.open_shulker_box", "Shulker Boxes Opened");
        translation.put("stat.minecraft.pig_one_cm", "Distance by Pig");
        translation.put("stat.minecraft.play_noteblock", "Note Blocks Played");
        translation.put("stat.minecraft.play_record", "Music Discs Played");
        translation.put("stat.minecraft.play_time", "Time Played");
        translation.put("stat.minecraft.player_kills", "Player Kills");
        translation.put("stat.minecraft.pot_flower", "Plants Potted");
        translation.put("stat.minecraft.raid_trigger", "Raids Triggered");
        translation.put("stat.minecraft.raid_win", "Raids Won");
        translation.put("stat.minecraft.ring_bell", "Bells Rung");
        translation.put("stat.minecraft.sleep_in_bed", "Times Slept in a Bed");
        translation.put("stat.minecraft.sneak_time", "Sneak Time");
        translation.put("stat.minecraft.sprint_one_cm", "Distance Sprinted");
        translation.put("stat.minecraft.strider_one_cm", "Distance by Strider");
        translation.put("stat.minecraft.swim_one_cm", "Distance Swum");
        translation.put("stat.minecraft.talked_to_villager", "Talked to Villagers");
        translation.put("stat.minecraft.target_hit", "Targets Hit");
        translation.put("stat.minecraft.time_since_death", "Time Since Last Death");
        translation.put("stat.minecraft.time_since_rest", "Time Since Last Rest");
        translation.put("stat.minecraft.total_world_time", "Time with World Open");
        translation.put("stat.minecraft.traded_with_villager", "Traded with Villagers");
        translation.put("stat.minecraft.treasure_fished", "Treasure Fished");
        translation.put("stat.minecraft.trigger_trapped_chest", "Trapped Chests Triggered");
        translation.put("stat.minecraft.tune_noteblock", "Note Blocks Tuned");
        translation.put("stat.minecraft.use_cauldron", "Water Taken from Cauldron");
        translation.put("stat.minecraft.walk_on_water_one_cm", "Distance Walked on Water");
        translation.put("stat.minecraft.walk_one_cm", "Distance Walked");
        translation.put("stat.minecraft.walk_under_water_one_cm", "Distance Walked under Water");

        translation.put("stat_type.minecraft.broken", "Times Broken");
        translation.put("stat_type.minecraft.crafted", "Times Crafted");
        translation.put("stat_type.minecraft.dropped", "Times Dropped");
        translation.put("stat_type.minecraft.mined", "Times Mined");
        translation.put("stat_type.minecraft.picked_up", "Times Picked Up");
        translation.put("stat_type.minecraft.used", "Times Used");

        return translation;
    }

    public static Map<String, String> getTRANSLATION() {
        return TRANSLATION;
    }
}
