package me.koenn.tb.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.koenn.tb.commands.CommandHandler;
import me.koenn.tb.exp.ExpSystem;
import me.koenn.tb.listeners.BotListener;
import me.koenn.tb.listeners.StreamerListener;
import me.koenn.tb.util.BotUtil;
import me.koenn.tb.util.FollowerUtil;
import me.koenn.tb.util.Message;
import me.koenn.tb.util.MessageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class MainGui extends Application {

    public static boolean scroll = true;
    public static VBox main;
    public static TextFlow blank;
    public static Scene mainScene;
    public static Stage primaryStage;
    public static UserList userList;
    private static ListView<TextFlow> chat;
    private static ListView<TextFlow> streamerChat;
    private static HashMap<String, Color> userColor = new HashMap<>();

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public static void setLastMessage(String user, String message) {
        try {
            if (chat.getItems().get(0).equals(blank)) {
                chat.getItems().clear();
            }
            Text username = new Text(user);
            Random random = new Random();
            if (!userColor.containsKey(user)) {
                Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                userColor.put(user, color);
            }
            username.setFill(userColor.get(user));
            Text text = new Text(": " + message);
            text.setFill(Color.WHITE);
            Date now = new Date();
            SimpleDateFormat stamp = new SimpleDateFormat("HH:mm ");
            Text timestamp = new Text(stamp.format(now));
            timestamp.setFill(Color.GRAY);
            Text followed = new Text("\u25A0 ");
            boolean mod = false;
            for (String string : UserList.moderatorList) {
                String check = string.replace("MOD ", "");
                if (check.equalsIgnoreCase(user)) {
                    mod = true;
                }
            }
            if (mod) {
                followed.setFill(Color.PURPLE);
            } else {
                if (FollowerUtil.currentFollowers.contains(user)) {
                    followed.setFill(Color.GREEN);
                } else {
                    followed.setFill(Color.RED);
                }
            }
            TextFlow textFlow = new TextFlow(timestamp, followed, username, text);
            chat.getItems().add(textFlow);
        } catch (Exception ex) {
            System.out.println("Error in thread");
        }
    }

    public static void setLastStreamerMessage(String user, String message) {
        try {
            if (streamerChat.getItems().get(0).equals(blank)) {
                streamerChat.getItems().clear();
            }
            Text username = new Text(user);
            Random random = new Random();
            if (!userColor.containsKey(user)) {
                Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                userColor.put(user, color);
            }
            username.setFill(userColor.get(user));
            Text text = new Text(": " + message);
            text.setFill(Color.WHITE);
            Date now = new Date();
            SimpleDateFormat stamp = new SimpleDateFormat("HH:mm ");
            Text timestamp = new Text(stamp.format(now));
            timestamp.setFill(Color.GRAY);
            TextFlow textFlow = new TextFlow(timestamp, username, text);
            streamerChat.getItems().add(textFlow);
        } catch (Exception ex) {
            System.out.println("Error in thread");
        }
    }

    public static ListView<TextFlow> getChat() {
        return chat;
    }

    @Override
    public void start(Stage stage) throws Exception {
        new ExpSystem().init();
        primaryStage = stage;
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.setupCommands();
        new MessageUtil(commandHandler);
        blank = new TextFlow();
        primaryStage.setTitle("Twitch Bot");
        primaryStage.setOnCloseRequest(event -> closeProgram());
        primaryStage.getIcons().add(0, new Image("http://orig05.deviantart.net/377c/f/2013/134/b/2/twitch_tv_logo_by_pixpox-d65akmn.png"));
        primaryStage.setMaximized(false);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(new BotMenu());
        mainLayout.setLeft(new ButtonGrid());

        main = new VBox(1);
        main.setAlignment(Pos.CENTER);

        VBox chatBox = new VBox(5);
        chatBox.setAlignment(Pos.CENTER);
        chat = new ListView<>();
        TextField chatInput = new TextField();
        chatInput.setPadding(new Insets(0, 0, 6, 0));
        chatInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                MessageUtil.sendMessage(new Message(BotListener.channel, chatInput.getText()));
                chatInput.setText("");
            }
        });
        chat.setOnMouseClicked(e -> chatInput.selectHome());
        chat.getItems().add(blank);
        chatBox.getChildren().addAll(chat, chatInput);
        chatBox.setPadding(new Insets(5, 0, 0, 50));

        VBox streamerChatBox = new VBox(5);
        chatBox.setAlignment(Pos.CENTER);
        streamerChat = new ListView<>();
        TextField streamerChatInput = new TextField();
        streamerChatInput.setPadding(new Insets(0, 0, 6, 0));
        streamerChatInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                MessageUtil.sendStreamerMessage(new Message(StreamerListener.channel, streamerChatInput.getText()));
                streamerChatInput.setText("");
            }
        });
        streamerChat.setOnMouseClicked(e -> streamerChatInput.selectHome());
        streamerChat.getItems().add(blank);
        streamerChatBox.getChildren().addAll(streamerChat, streamerChatInput);
        streamerChatBox.setPadding(new Insets(5, 0, 0, 50));

        Tabs tabs = new Tabs();
        tabs.addTab("Chat", chatBox);
        tabs.addTab("Streamer", streamerChatBox);
        tabs.addTab("Scheduler", new Scheduler());
        tabs.addTab("Exp", new ExpGui());

        userList = new UserList();

        main.getChildren().addAll(tabs, chatBox);
        mainLayout.setCenter(main);
        mainLayout.setRight(userList);
        BorderPane.setAlignment(main, Pos.BOTTOM_CENTER);
        mainScene = new Scene(mainLayout, 900, 500);
        mainScene.getStylesheets().add("me/koenn/tb/stylesheet/twitchbot.css");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.centerOnScreen();

        Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            try {
                if (scroll) {
                    chat.scrollTo(chat.getItems().size() - 1);
                    streamerChat.scrollTo(streamerChat.getItems().size() - 1);
                    scroll = false;
                }
            } catch (Exception ex) {
                System.out.println("Not able to scroll");
            }
        }));
        scheduled.setCycleCount(Timeline.INDEFINITE);
        scheduled.play();
        BotLogin botLogin = new BotLogin(false);
        if (botLogin.loggedIn) {
            mainScene.setCursor(Cursor.WAIT);
            chat.getItems().remove(blank);
            Text blankText = new Text(">> Loading bot... Please wait");
            blankText.setFill(Color.WHITE);
            blank = new TextFlow(blankText);
            chat.getItems().add(blank);
            BotUtil.login = botLogin;
            BotUtil.startBot();
            userList.start();
        }
    }

    public void closeProgram() {
        BotUtil.forceStopBot();
    }
}
