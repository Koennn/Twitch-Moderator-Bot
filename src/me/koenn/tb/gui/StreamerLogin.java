package me.koenn.tb.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.koenn.tb.util.BotUtil;

public class StreamerLogin {

    public static StreamerLogin login;

    private TextField nameInput;
    private TextField passwordInput;
    private TextField channelInput;

    public StreamerLogin() {
        Stage stage = new Stage();
        stage.setTitle("Log in");
        stage.initModality(Modality.APPLICATION_MODAL);
        nameInput = new TextField("");
        nameInput.setPromptText("Username");
        passwordInput = new TextField("");
        passwordInput.setPromptText("OAuth Password");
        channelInput = new TextField("");
        channelInput.setPromptText("Stream name");

        Button button = new Button("Log in");
        button.setOnAction(e -> {
            if (nameInput.getText().equals("") || passwordInput.getText().equals("") || channelInput.getText().equals("")) {
                return;
            }
            login = this;
            BotUtil.startStreamer();
            stage.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(nameInput, passwordInput, channelInput, button);

        Scene scene = new Scene(layout, 300, 250);
        scene.getStylesheets().add("me/koenn/tb/stylesheet/twitchbot.css");
        stage.setScene(scene);
        stage.showAndWait();
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
