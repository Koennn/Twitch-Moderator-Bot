package me.koenn.tb.gui;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.koenn.tb.util.BotUtil;
import me.koenn.tb.util.Utils;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BotLogin {

    public boolean loggedIn = false;
    private TextField nameInput;
    private TextField passwordInput;
    private TextField channelInput;
    private Stage stage;

    public BotLogin(boolean show) {
        stage = new Stage();
        stage.setTitle("Log in");
        stage.initModality(Modality.APPLICATION_MODAL);
        Path botLoginFile = Utils.getDataDir().resolve("cache/bot_login.txt");
        nameInput = new TextField("");
        nameInput.setPromptText("Username");
        passwordInput = new TextField("");
        passwordInput.setPromptText("OAuth Password");
        channelInput = new TextField("");
        channelInput.setPromptText("Streamer name");
        try {
            List<String> lines = FileUtils.readLines(botLoginFile.toFile());
            for (String line : lines) {
                if (line.startsWith("Username: ")) {
                    nameInput.setText(line.split(" ")[1]);
                }
                if (line.startsWith("Password: ")) {
                    passwordInput.setText(line.split(" ")[1]);
                }
                if (line.startsWith("Channel: ")) {
                    channelInput.setText(line.split(" ")[1]);
                }
                loggedIn = true;
            }
        } catch (Exception ex) {
            System.out.println("No cached login data found");
            loggedIn = false;
            if (!show) {
                return;
            }
        }
        Button button = new Button("Log in");
        button.setOnMouseClicked(e -> click());
        nameInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                click();
            }
        });
        passwordInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                click();
            }
        });
        channelInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                click();
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(nameInput, passwordInput, channelInput, button);

        Scene scene = new Scene(layout, 300, 250);
        scene.getStylesheets().add("me/koenn/tb/stylesheet/twitchbot.css");
        stage.setScene(scene);
        if (show) {
            stage.showAndWait();
        }
    }

    private void click() {
        Path botLoginFile = Utils.getDataDir().resolve("cache/bot_login.txt");
        if (nameInput.getText().equals("") || passwordInput.getText().equals("") || channelInput.getText().equals("")) {
            return;
        }
        MainGui.mainScene.setCursor(Cursor.WAIT);
        MainGui.getChat().getItems().remove(MainGui.blank);
        Text blankText = new Text(">> Loading bot... Please wait");
        blankText.setFill(Color.WHITE);
        MainGui.blank = new TextFlow(blankText);
        MainGui.getChat().getItems().add(MainGui.blank);
        BotUtil.login = this;
        MainGui.userList.start();
        BotUtil.startBot();
        stage.close();
        try {
            List<String> lines = new ArrayList<>();
            lines.add("Username: " + nameInput.getText());
            lines.add("Password: " + passwordInput.getText());
            lines.add("Channel: " + channelInput.getText());
            FileUtils.writeLines(botLoginFile.toFile(), lines);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getBotName() {
        return nameInput.getText();
    }

    public String getPassword() {
        return passwordInput.getText();
    }

    public String getChannel() {
        return channelInput.getText();
    }
}
