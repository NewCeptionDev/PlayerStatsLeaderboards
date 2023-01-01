package dev.newception.playerStatsLeaderboards;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.newception.playerStatsLeaderboards.commands.AvailableLeaderboardsCommand;
import dev.newception.playerStatsLeaderboards.commands.ShowLeaderboardCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.stat.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class PlayerStatsLeaderboardsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("player-stats-leaderboards");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> AvailableLeaderboardsCommand.register(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ShowLeaderboardCommand.register(dispatcher));

		LOGGER.info("Hello Fabric world!");
	}
}
