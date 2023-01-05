package dev.newception.playerStatsLeaderboards;

import dev.newception.playerStatsLeaderboards.commands.AvailableLeaderboardsCommand;
import dev.newception.playerStatsLeaderboards.commands.ShowLeaderboardCommand;
import dev.newception.playerStatsLeaderboards.config.Config;
import dev.newception.playerStatsLeaderboards.config.DefaultConfig;
import dev.newception.playerStatsLeaderboards.events.PlayerJoinEventListener;
import dev.newception.playerStatsLeaderboards.util.PlayerInformationCache;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		Config readConfig = Config.readConfig();

		if(readConfig != null) {
			MOD_CONFIG = readConfig;
			LOGGER.info("Successfully read existing config file");
		} else {
			MOD_CONFIG = new DefaultConfig();
			DefaultConfig.persistConfig(MOD_CONFIG);
		}

		REGISTERED_PLAYERS = getUUIDsFromAllPlayersEveryJoined();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> AvailableLeaderboardsCommand.register(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ShowLeaderboardCommand.register(dispatcher));

		ServerPlayConnectionEvents.JOIN.register(PlayerJoinEventListener::handlePlayerJoinEvent);

		LOGGER.info("Hello Fabric world!");
	}

	public static Set<UUID> getUUIDsFromAllPlayersEveryJoined() {
		String path = "./world/stats/";

		if(!new File(path).exists()) {
			return new HashSet<>();
		}

		return Stream.of(Objects.requireNonNull(new File(path).listFiles()))
				.filter(file -> !file.isDirectory())
				.map(File::getName)
				.filter(s -> !s.isEmpty())
				.map(s -> s.split("\\.")[0])
				.map(UUID::fromString)
				.collect(Collectors.toSet());
	}
}
