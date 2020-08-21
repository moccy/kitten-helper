package com.lyut.kittenhelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Kitten Helper"
)
public class KittenHelperPlugin extends Plugin
{
	private static final int FollowerVarPlayerId = 447;
	private Feline Feline = null;

	@Inject
	private Client client;

	@Inject
	private KittenHelperConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Kitten Helper started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Kitten Helper stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Kitten Helper says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// Skip if not logged in
		if(client.getGameState() != GameState.LOGGED_IN) {
			return;
		}

		// Skip if the varbit change isn't related to a follower.
		if(event.getIndex() != FollowerVarPlayerId) {
			return;
		}

		int varpValue = client.getVarpValue(FollowerVarPlayerId);

		if(HasFollower()) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Hello kitty!", null);
			Feline = new Feline(getFollowerId(varpValue));
			log.info("Detected follower with id " + Feline.FollowerId);
		} else {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Goodbye kitty!", null);
			Feline = null;
			log.info("Follower has been dismissed");
		}
	}

	@Provides
	KittenHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KittenHelperConfig.class);
	}

	private Boolean HasFollower() {
		return client.getVarpValue(FollowerVarPlayerId) != -1;
	}

	private String getFollowerBinaryString(int followerVarPlayerValue) {
		String binaryString = Integer.toBinaryString(followerVarPlayerValue);
		while(binaryString.length() < 32) {
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}

	private int getFollowerId(String followerBinaryString) {
		// The first two octets of the binary string converted to decimal represent the ID.
		if(followerBinaryString.length() != 32) {
			throw new IllegalArgumentException("Expected a binary string of length 32, instead found a length of " + followerBinaryString.length());
		}
		return Integer.parseInt(followerBinaryString.substring(0, 16), 2);
	}

	private int getFollowerId(int followerVarPlayerValue) {
		return getFollowerId(getFollowerBinaryString(followerVarPlayerValue));
	}

	private boolean isKitten(int followerId) {
		return followerId >= 5591 && followerId <= 5597;
	}
}
