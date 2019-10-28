package org.nologic.oisin.minecraftdiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class MinecraftDiscord extends JavaPlugin implements Listener, DiscordEventReceiver {
    // reference to discord connection object
    DiscordAdaptor dca;
    Configuration config;

    @Override
    public void onEnable() {
        getLogger().info("Yay .... MinecraftDiscord is enabled");
        config = new Configuration();
        dca = new DiscordAdaptor(config);
        dca.registerDiscordEventReceiver(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Boo .... MinecraftDiscord is disabled");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
        // send event to discord connection object
        getLogger().info("Got a chat message: " + chatEvent.getMessage());
            dca.sendMessage("<" + chatEvent.getPlayer().getDisplayName() + "> " + chatEvent.getMessage());
    }

    @Override
    public void receiveMessage(String message) {
        getLogger().info("Got a message from discord: " + message);
        getServer().broadcastMessage(message);
    }
}

