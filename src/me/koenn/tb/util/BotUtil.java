package me.koenn.tb.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import me.koenn.tb.bot.StreamerBot;
import me.koenn.tb.bot.TwitchBot;
import me.koenn.tb.gui.AlertBox;
import me.koenn.tb.gui.BotLogin;
import me.koenn.tb.gui.MainGui;
import me.koenn.tb.gui.StreamerLogin;


@SuppressWarnings("EmptyCatchBlock")
public class BotUtil {

    public static BotLogin login;

    public static void startBot() {
        if (login == null) {
            AlertBox.display("Error!", "You need to login on the bot's account");
        }
        try {
            new Thread(new TwitchBot(login.getBotName(), login.getPassword(), login.getChannel())).start();
            Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                MainGui.mainScene.setCursor(Cursor.DEFAULT);
                Text blankText = new Text(">> Bot enabled!");
                blankText.setFill(Color.WHITE);
                MainGui.blank = new TextFlow(blankText);
                MainGui.getChat().getItems().add(MainGui.blank);
            }));
            scheduled.setCycleCount(1);
            scheduled.play();
        } catch (IllegalArgumentException ex) {
            AlertBox.display("Error!", "Bot is already running");
        }
    }

    public static void startStreamer() {
        if (StreamerLogin.login == null) {
            AlertBox.display("Error!", "You need to login on your account");
        }
        try {
            new Thread(new StreamerBot(StreamerLogin.login.getBotName(), StreamerLogin.login.getPassword(), StreamerLogin.login.getChannel())).start();
        } catch (IllegalArgumentException ex) {
            AlertBox.display("Error!", "You are already online");
        }
    }

    public static void stopStreamer() {
        if (StreamerBot.runningBot == null) {
            AlertBox.display("Error!", "You are not online");
        } else {
            StreamerBot.runningBot.getBot().close();
            StreamerBot.runningBot = null;
        }
    }

    public static void stopBot() {
        if (TwitchBot.runningBot == null) {
            AlertBox.display("Error!", "Bot not running");
        } else {
            TwitchBot.runningBot.getBot().close();
            MainGui.getChat().getItems().clear();
            MainGui.getChat().getItems().add(MainGui.blank);
            TwitchBot.runningBot = null;
        }
    }

    public static void forceStopBot() {
        try {
            if (TwitchBot.runningBot != null) {
                TwitchBot.runningBot.getBot().close();
                TwitchBot.runningBot = null;
            }
        } catch (Exception ex) {
        }
    }
}
