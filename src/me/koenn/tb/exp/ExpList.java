package me.koenn.tb.exp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

public class ExpList extends TableView<Viewer> {

    @SuppressWarnings("unchecked")
    public ExpList() {
        super();
        ObservableList<Viewer> teamMembers = FXCollections.observableArrayList();
        for (String user : ExpSystem.exp.keySet()) {
            teamMembers.add(new Viewer(user, ExpSystem.exp.get(user), ExpSystem.time.get(user)));
        }
        this.setItems(teamMembers);

        TableColumn<Viewer, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory("username"));
        username.setMinWidth(185);

        TableColumn<Viewer, Integer> exp = new TableColumn<>("Exp");
        exp.setCellValueFactory(new PropertyValueFactory("exp"));
        exp.setMinWidth(185);

        TableColumn<Viewer, Double> time = new TableColumn<>("Time");
        time.setCellValueFactory(new PropertyValueFactory("time"));
        time.setMinWidth(185);

        this.getColumns().setAll(username, exp, time);

        Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            teamMembers.clear();
            for (String user : ExpSystem.exp.keySet()) {
                teamMembers.add(new Viewer(user, ExpSystem.exp.get(user), ExpSystem.time.get(user)));
            }
        }));
        scheduled.setCycleCount(Timeline.INDEFINITE);
        scheduled.play();
    }
}
