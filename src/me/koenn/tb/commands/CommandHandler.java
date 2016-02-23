package me.koenn.tb.commands;

import me.koenn.tb.commands.commands.Test;
import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.ArrayList;

public class CommandHandler {

    private static ArrayList<ChatCommand> commands = new ArrayList<>();

    public void onCommand(Channel channel, User sender, String command) {
        commands.stream().filter(chatCommand -> command.startsWith(chatCommand.getCommand())).forEach(chatCommand -> chatCommand.execute(channel, sender, command));
    }

    public void setupCommands() {
        commands.add(new Test());
    }
}
