package me.koenn.tb.bot;

import me.koenn.tb.listeners.StreamerListener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class StreamerBot implements Runnable {

    public static StreamerBot runningBot;

    private PircBotX bot;
    private Configuration configuration;

    @SuppressWarnings("deprecation")
    public StreamerBot(String username, String password, String streamer) {
        if (runningBot != null) {
            throw new IllegalArgumentException("Bot is already running");
        }
        runningBot = this;
        this.configuration = new Configuration.Builder()
                .setName(username)
                .setServerHostname("irc.freenode.net")
                .setServerPassword(password)
                .addAutoJoinChannel("#" + streamer.toLowerCase())
                .addListener(new StreamerListener())
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
