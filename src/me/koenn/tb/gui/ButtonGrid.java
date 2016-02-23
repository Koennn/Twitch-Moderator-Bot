package me.koenn.tb.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

@SuppressWarnings("unused")
public class ButtonGrid extends GridPane {

    public ButtonGrid() {
        super();
        this.setPadding(new Insets(50, 0, 0, 50));

    }

    private void addButton(int x, int z, String name, EventHandler<ActionEvent> onClick) {
        Button button = new Button(name);
        button.setOnAction(onClick);
        this.add(button, x, z);
    }
}
