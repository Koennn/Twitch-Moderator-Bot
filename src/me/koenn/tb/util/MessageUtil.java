package me.koenn.tb.util;

import me.koenn.tb.bot.StreamerBot;
import me.koenn.tb.bot.TwitchBot;
import me.koenn.tb.commands.CommandHandler;
import me.koenn.tb.gui.MainGui;
import me.koenn.tb.gui.StreamerLogin;

public class MessageUtil {

    private static CommandHandler commandHandler;

    public MessageUtil(CommandHandler handler) {
        commandHandler = handler;
    }

    public static void sendMessage(Message message) {
        if (TwitchBot.runningBot == null) {
            return;
        }
        if (!message.getMessage().startsWith("!")) {
            MessageTimer.sendNewMessage(message);
        } else {
            commandHandler.onCommand(message.getChannel(), TwitchBot.runningBot.getBot().getUserBot(), message.getMessage());
        }
        try {
            MainGui.setLastMessage(BotUtil.login.getBotName(), message.getMessage());
            MainGui.scroll = true;
        } catch (Exception ex) {
            System.out.println("Error while sending message!");
        }
    }

    public static void sendStreamerMessage(Message message) {
        if (StreamerBot.runningBot == null) {
            return;
        }
        MessageTimer.sendNewMessage(message);
        try {
            MainGui.setLastStreamerMessage(StreamerLogin.login.getBotName(), message.getMessage());
            MainGui.scroll = true;
        } catch (Exception ex) {
            System.out.println("Error while sending message!");
        }
    }

    public static void sendRealMessage(Message message) {
        message.getChannel().send().message(message.getMessage());
    }
}
