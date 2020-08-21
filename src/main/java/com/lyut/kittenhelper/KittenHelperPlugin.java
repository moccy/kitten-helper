package com.lyut.kittenhelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
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

	@Provides
	KittenHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KittenHelperConfig.class);
	}
}
