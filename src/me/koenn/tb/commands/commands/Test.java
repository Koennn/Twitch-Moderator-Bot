package me.koenn.tb.commands.commands;

import me.koenn.tb.commands.ChatCommand;
import me.koenn.tb.util.Message;
import me.koenn.tb.util.MessageUtil;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class Test implements ChatCommand {

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public void execute(Channel channel, User sender, String command) {
        MessageUtil.sendMessage(new Message(channel, "Test successful!"));
    }
}
