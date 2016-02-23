package me.koenn.tb.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import me.koenn.tb.util.BotUtil;

import java.util.ArrayList;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class BotMenu extends MenuBar {

    private ArrayList<Menu> menus = new ArrayList<>();

    public BotMenu() {
        ArrayList<MenuItem> botItems = new ArrayList<>();
        botItems.add(this.mkMenuItem("Login", event -> new BotLogin(true)));
        botItems.add(this.mkMenuItem("Start", event -> BotUtil.startBot()));
        botItems.add(this.mkMenuItem("Stop", event -> BotUtil.stopBot()));
        this.addMenu("Bot", botItems);
        ArrayList<MenuItem> streamerItems = new ArrayList<>();
        streamerItems.add(this.mkMenuItem("Login", event -> new StreamerLogin()));
        this.addMenu("Streamer", streamerItems);
    }

    private void addMenu(String name, ArrayList<MenuItem> menuItems) {
        Menu menu = new Menu(name);
        menus.add(menu);
        menu.getItems().addAll(menuItems);
        this.getMenus().add(menu);
    }

    private MenuItem mkMenuItem(String name, EventHandler<ActionEvent> onClick) {
        MenuItem item = new MenuItem(name);
        item.setOnAction(onClick);
        return item;
    }
}
