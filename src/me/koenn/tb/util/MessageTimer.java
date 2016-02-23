package me.koenn.tb.util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MessageTimer {

    private static ArrayList<MessageTimer> messageTimers = new ArrayList<>();
    private static ArrayList<Message> scheduledMessages = new ArrayList<>();

    private int messages;

    public MessageTimer(Message message) {
        this.messages = 1;
        messageTimers.add(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                stop();
            }
        }, 30000);
        sendMessage(message);
    }

    public static void sendMessage(Message message) {
        for (MessageTimer timer : messageTimers) {
            if (timer.trySendMessage(message)) {
                return;
            }
        }
    }

    public static void sendNewMessage(Message message) {
        if (messageTimers.isEmpty()) {
            scheduledMessages.clear();
            new MessageTimer(message);
        } else {
            sendMessage(message);
        }
    }

    public void stop() {
        if (messageTimers.contains(this)) {
            messageTimers.remove(this);
            if (scheduledMessages.isEmpty()) {
                return;
            }
            for (int i = 0; i < scheduledMessages.size(); i++) {
                new MessageTimer(scheduledMessages.get(i));
                if (i < scheduledMessages.size()) {
                    scheduledMessages.remove(scheduledMessages.get(i));
                }
                scheduledMessages.clear();
            }
        }
    }

    public boolean trySendMessage(Message message) {
        if (messages < 20) {
            System.out.println("!!Send " + messages + " messages");
            messages++;
            MessageUtil.sendRealMessage(message);
            scheduledMessages.remove(message);
            return true;
        } else {
            System.out.println("!!Scheduled");
            scheduledMessages.add(message);
            for (Message scheduled : scheduledMessages) {
                System.out.println("-> " + scheduled);
            }
            return false;
        }
    }
}
