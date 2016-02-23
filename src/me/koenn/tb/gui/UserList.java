package me.koenn.tb.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import me.koenn.tb.util.FollowerUtil;
import me.koenn.tb.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class UserList extends VBox {

    public static ListView<TextFlow> userList = new ListView<>();
    public static List<String> usernameList = new ArrayList<>();
    public static ArrayList<String> moderatorList = new ArrayList<>();
    private static boolean refresh;

    public UserList() {
        super(1);
        refresh = false;
        userList.getItems().add(new TextFlow());
        this.getChildren().add(userList);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 50, 0, 10));
        this.setMinWidth(300);
    }


    public void start() {
        System.out.println("[MAIN] Loading viewer parser...");
        Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            System.out.println("[ViewerParser] Starting new thread...");
            new Thread(() -> {
                System.out.println("[ViewerParser] Getting Information From Twitch Chatter API");
                try {
                    usernameList.clear();
                    String viewerInformation = Utils.urlToString("https://tmi.twitch.tv/group/user/jermaino_o/chatters");
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(viewerInformation);
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray moderators = (JSONArray) ((JSONObject) jsonObject.get("chatters")).get("moderators");
                    for (Object viewer : moderators) {
                        usernameList.add("MOD " + viewer.toString().toLowerCase());
                        moderatorList.add("MOD " + viewer.toString().toLowerCase());
                    }
                    JSONArray viewers = (JSONArray) ((JSONObject) jsonObject.get("chatters")).get("viewers");
                    for (Object viewer : viewers) {
                        usernameList.add((String) viewer);
                    }
                    refresh = true;
                    System.out.println("[ViewerParser] Loaded new viewer list");
                } catch (Exception e) {
                    System.err.println("[ViewerParser] Couldn't Connect To Twitch API!\nCause: " + e.getMessage() + " <- " + e.getCause());
                }
            }).start();
        }));
        scheduled.setCycleCount(Timeline.INDEFINITE);
        scheduled.play();

        Timeline scheduled2 = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            try {
                if (refresh) {
                    System.out.println("[MAIN] Refreshing viewer list...");
                    if (!usernameList.isEmpty()) {
                        userList.getItems().clear();
                        for (String user : usernameList) {
                            Text text = new Text(user.split(" ")[user.split(" ").length - 1]);
                            text.setFill(Color.WHITE);
                            Text prefix = new Text("\u25A0 ");
                            if (user.split(" ").length > 1) {
                                prefix.setFill(Color.PURPLE);
                            } else {
                                if (FollowerUtil.currentFollowers.contains(user)) {
                                    prefix.setFill(Color.GREEN);
                                } else {
                                    prefix.setFill(Color.RED);
                                }
                            }
                            userList.getItems().add(new TextFlow(prefix, text));
                        }
                        if (userList.getItems().isEmpty()) {
                            userList.getItems().addAll(new TextFlow(new Text()));
                        }
                        System.out.println("[MAIN] Done!");
                        refresh = false;
                    }
                }
            } catch (ConcurrentModificationException ex) {
                System.err.println("[MAIN] Unable to refresh list.");
            }
        }));
        scheduled2.setCycleCount(Timeline.INDEFINITE);
        scheduled2.play();
    }
}
