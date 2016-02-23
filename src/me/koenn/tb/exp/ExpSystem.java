package me.koenn.tb.exp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import me.koenn.tb.bot.TwitchBot;
import me.koenn.tb.gui.UserList;
import me.koenn.tb.util.Utils;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class ExpSystem {

    public static HashMap<String, Integer> exp = new HashMap<>();
    public static HashMap<String, Integer> time = new HashMap<>();
    private static Path expSaveFile;

    private static ArrayList<String> lastUsers = new ArrayList<>();
    private static ArrayList<String> newUsers = new ArrayList<>();

    public ExpSystem() {
        expSaveFile = Utils.getDataDir().resolve("exp/exp_main.txt");
        Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            if (TwitchBot.runningBot == null) {
                return;
            }
            for (String user : UserList.usernameList) {
                String username = user.split(" ")[user.split(" ").length - 1].toLowerCase();
                if (lastUsers.contains(username)) {
                    if (exp.containsKey(username)) {
                        exp.put(username, exp.get(username) + 1);
                    } else {
                        exp.put(username, 1);
                    }
                    if (time.containsKey(username)) {
                        time.put(username, time.get(username) + 5);
                    } else {
                        time.put(username, 5);
                    }
                } else {
                    newUsers.add(username);
                }
            }
            lastUsers.addAll(newUsers);
            newUsers.clear();
            saveToFile();
        }));
        scheduled.setCycleCount(Timeline.INDEFINITE);
        scheduled.play();
    }

    public static void addExp(String user, int amount) {
        if (exp.containsKey(user)) {
            exp.put(user, exp.get(user) + amount);
        } else {
            exp.put(user, amount);
        }
        saveToFile();
    }

    public static void removeExp(String user, int amount) throws IllegalArgumentException {
        if (exp.containsKey(user)) {
            exp.put(user, exp.get(user) - amount);
        } else {
            throw new IllegalArgumentException("User has no exp");
        }
        saveToFile();
    }

    private static void saveToFile() {
        try {
            List<String> parsedExp = new ArrayList<>();
            for (String user : exp.keySet()) {
                parsedExp.add("$" + user + ":" + exp.get(user) + ":" + time.get(user));
            }
            FileUtils.writeLines(expSaveFile.toFile(), parsedExp);
        } catch (IOException ex) {
            System.err.println("[MAIN] [EXP] Not able to write to exp_main.txt");
        }
    }

    private static void loadFromFile() {
        exp.clear();
        time.clear();
        try {
            for (String line : FileUtils.readLines(expSaveFile.toFile())) {
                if (line.startsWith("$")) {
                    exp.put(line.split(":")[0].replace("$", ""), Integer.parseInt(line.split(":")[1]));
                    time.put(line.split(":")[0].replace("$", ""), Integer.parseInt(line.split(":")[2]));
                }
            }
        } catch (IOException ex) {
            System.err.println("[MAIN] [EXP] Not able to read exp_main.txt");
        } catch (NumberFormatException ex) {
            System.err.println("[MAIN] [EXP] Unable to parse exp value in exp_main.txt");
        }
    }

    public void init() {
        loadFromFile();
    }
}
