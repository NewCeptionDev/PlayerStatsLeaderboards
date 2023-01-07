package dev.newception.playerStatsLeaderboards;

import dev.newception.playerStatsLeaderboards.commands.LeaderboardSelectionCommand;
import dev.newception.playerStatsLeaderboards.commands.ShowSpecificLeaderboardCommand;
import dev.newception.playerStatsLeaderboards.config.Config;
import dev.newception.playerStatsLeaderboards.config.DefaultConfig;
import dev.newception.playerStatsLeaderboards.events.PlayerJoinEventListener;
import dev.newception.playerStatsLeaderboards.io.ConfigFileIO;
import dev.newception.playerStatsLeaderboards.io.StatsFileReader;
import dev.newception.playerStatsLeaderboards.util.PlayerInformationCache;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PlayerStatsLeaderboardsMod implements ModInitializer {

	public static final String MOD_ID = "player-stats-leaderboards";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Set<UUID> REGISTERED_PLAYERS = new HashSet<>();

	public static Map<UUID, PlayerInformationCache> PLAYER_INFORMATION_CACHE = new HashMap<>();

	public static Config MOD_CONFIG;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Config readConfig = ConfigFileIO.readConfig();

		if(readConfig != null) {
			MOD_CONFIG = readConfig;
			LOGGER.info("Successfully read existing config file");
		} else {
			MOD_CONFIG = new DefaultConfig();
			ConfigFileIO.persistConfig(MOD_CONFIG);
		}

		REGISTERED_PLAYERS = StatsFileReader.getUUIDsFromAllPlayersEveryJoined();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> LeaderboardSelectionCommand.register(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ShowSpecificLeaderboardCommand.register(dispatcher));

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> PlayerJoinEventListener.handlePlayerJoinEvent(handler));

		LOGGER.info("Successfully started PlayerStatsLeaderboards Mod!");
	}
}
