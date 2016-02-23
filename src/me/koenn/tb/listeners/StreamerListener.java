package me.koenn.tb.listeners;

import me.koenn.tb.gui.MainGui;
import me.koenn.tb.gui.StreamerLogin;
import org.pircbotx.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

@SuppressWarnings("ConstantConditions")
public class StreamerListener extends ListenerAdapter {

    public static Channel channel;

    @Override
    public void onJoin(JoinEvent event) throws Exception {
        if (event.getUser().getNick().equals(StreamerLogin.login.getBotName())) {
            channel = event.getChannel();
        }
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        MainGui.setLastStreamerMessage(event.getUser().getNick(), event.getMessage());
        MainGui.scroll = true;
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        MainGui.setLastStreamerMessage(event.getUser().getNick(), event.getMessage());
        MainGui.scroll = true;
    }
}
