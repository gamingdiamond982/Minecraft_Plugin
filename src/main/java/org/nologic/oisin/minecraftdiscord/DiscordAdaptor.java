package org.nologic.oisin.minecraftdiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Scanner;

public class DiscordAdaptor extends ListenerAdapter {
    private JDA discord;
    private TextChannel tc;
    private Configuration config;
    private ArrayList<DiscordEventReceiver> discordEventReceivers = new ArrayList<DiscordEventReceiver>();

    public DiscordAdaptor(Configuration config) {
        System.out.println("Token: " + config.getProperty("token"));
        this.config = config;
        try {
            discord = new JDABuilder(config.getProperty("token"))                               // Use provided token from command line arguments
                    .addEventListeners(this)                    // Start listening with this listener
                    .setActivity(Activity.playing("Crafting mines")) // Inform users that we are crafting mines
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)     // Please don't disturb us while we're crafting mines
                    .build();                                   // Login with these options
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {

            for (Guild guild : discord.awaitReady().getGuilds()) {
                System.out.println("GUILD: " + guild.getName());
                tc = guild.getTextChannelById(config.getProperty("channelID"));
            }
            tc = discord.awaitReady().getTextChannelById(config.getProperty("channelID"));
            tc.getManager().setTopic("Minecraft FTW");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        System.out.println("Got a message: " + message);
        tc.sendMessage(message).queue();
    }

    public void registerDiscordEventReceiver(DiscordEventReceiver r) {
        discordEventReceivers.add(r);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        for (DiscordEventReceiver der : discordEventReceivers) {
            if (msg.getAuthor().isBot() == false) {
                der.receiveMessage("<" + msg.getAuthor().getName() + "> " + msg.getContentDisplay());
            }
        }
    }

    public static void main(String[] args) {
        DiscordAdaptor da = new DiscordAdaptor(new Configuration());
        System.out.println("Got a discord adaptor: " + da.toString());
    }
}
