package me.koenn.tb.gui;

import javafx.scene.layout.BorderPane;
import me.koenn.tb.exp.ExpList;

public class ExpGui extends BorderPane {

    public ExpGui() {
        super();
        this.setCenter(new ExpList());
    }
}
