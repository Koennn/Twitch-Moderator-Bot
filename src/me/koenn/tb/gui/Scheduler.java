package me.koenn.tb.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.koenn.tb.listeners.BotListener;
import me.koenn.tb.util.Message;
import me.koenn.tb.util.MessageUtil;

import java.util.ArrayList;
import java.util.Random;

public class Scheduler extends VBox {

    private ArrayList<String> scheduledMessages = new ArrayList<>();

    public Scheduler() {
        super(1);
        addTask();
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(5, 0, 0, 50));
        Random random = new Random();
        Timeline scheduled = new Timeline(new KeyFrame(Duration.minutes(5), event -> {
            if (scheduledMessages.isEmpty()) {
                return;
            }
            MessageUtil.sendMessage(new Message(BotListener.channel, scheduledMessages.get(random.nextInt(scheduledMessages.size()))));
        }));
        scheduled.setCycleCount(Timeline.INDEFINITE);
        scheduled.play();
    }

    private void addTask() {
        HBox task = new HBox(1);
        Button save = new Button("Save");
        Button delete = new Button("Delete");
        TextField input = new TextField();
        if (MainGui.primaryStage.isMaximized()) {
            input.setMinWidth(850);
        } else {
            input.setMinWidth(390);
        }
        save.setOnAction(e -> {
            if (input.getText().equals("")) {
                return;
            }
            if (!scheduledMessages.contains(input.getText())) {
                scheduledMessages.add(input.getText());
                addTask();
            }
        });
        delete.setOnAction(e -> {
            if (scheduledMessages.contains(input.getText())) {
                scheduledMessages.remove(input.getText());
                this.getChildren().remove(task);
                input.setText("");
            }
        });
        task.getChildren().addAll(input, save, delete);
        this.getChildren().add(task);
    }
}
