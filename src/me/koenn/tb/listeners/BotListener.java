package me.koenn.tb.listeners;

import me.koenn.tb.commands.CommandHandler;
import me.koenn.tb.gui.MainGui;
import me.koenn.tb.util.BotUtil;
import me.koenn.tb.util.FollowerUtil;
import me.koenn.tb.util.Message;
import me.koenn.tb.util.MessageUtil;
import org.pircbotx.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

@SuppressWarnings("ConstantConditions")
public class BotListener extends ListenerAdapter {

    public static Channel channel;
    private CommandHandler handler;

    public BotListener(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onJoin(JoinEvent event) throws Exception {
        if (event.getUser().getNick().equals(BotUtil.login.getBotName())) {
            channel = event.getChannel();
            new Thread(new FollowerUtil()).start();
        } else {
            MessageUtil.sendMessage(new Message(event.getChannel(), "Welcome " + event.getUser().getNick() + " to the stream!"));
        }
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith("!")) {
            String command = event.getMessage().substring(1);
            handler.onCommand(event.getChannel(), event.getUser(), command);
        }
        MainGui.setLastMessage(event.getUser().getNick(), event.getMessage());
        MainGui.scroll = true;
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        MainGui.setLastMessage(event.getUser().getNick(), event.getMessage());
        MainGui.scroll = true;
    }
}
