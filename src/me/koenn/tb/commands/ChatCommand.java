package me.koenn.tb.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

public interface ChatCommand {

    String getCommand();

    void execute(Channel channel, User sender, String command);
}
