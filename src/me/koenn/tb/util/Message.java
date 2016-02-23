package me.koenn.tb.util;

import org.pircbotx.Channel;

public class Message {

    private Channel channel;
    private String message;

    public Message(Channel channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }
}
