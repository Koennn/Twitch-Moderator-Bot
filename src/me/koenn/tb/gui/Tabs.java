package me.koenn.tb.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Tabs extends HBox {

    public Tabs() {
        super(4);
        this.setPadding(new Insets(0, 0, 0, 50));
        this.setAlignment(Pos.CENTER_LEFT);
    }

    public void addTab(String text, Node show) {
        this.getChildren().add(mkButton(text, e -> {
            MainGui.main.getChildren().remove(1);
            MainGui.main.getChildren().add(show);
        }));
    }

    private Button mkButton(String text, EventHandler<ActionEvent> onClick) {
        Button button = new Button(text);
        button.setOnAction(onClick);
        return button;
    }
}
