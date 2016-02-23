package me.koenn.tb.bot;

import me.koenn.tb.commands.CommandHandler;
import me.koenn.tb.listeners.BotListener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class TwitchBot implements Runnable {

    public static TwitchBot runningBot;

    private PircBotX bot;
    private Configuration configuration;

    @SuppressWarnings("deprecation")
    public TwitchBot(String username, String password, String streamer) {
        if (runningBot != null) {
            throw new IllegalArgumentException("Bot is already running");
        }
        runningBot = this;
        CommandHandler handler = new CommandHandler();
        handler.setupCommands();
        this.configuration = new Configuration.Builder()
                .setName(username)
                .setServerHostname("irc.twitch.tv")
                .setServerPassword(password)
                .addAutoJoinChannel("#" + streamer.toLowerCase())
                .addListener(new BotListener(handler))
                .buildConfiguration();
    }

    @Override
    public void run() {
        try {
            this.bot = new PircBotX(this.configuration);
            this.bot.startBot();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }

    public PircBotX getBot() {
        return this.bot;
    }
}
