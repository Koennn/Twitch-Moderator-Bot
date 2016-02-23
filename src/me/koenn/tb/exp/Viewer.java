package me.koenn.tb.exp;

import javafx.beans.property.*;

@SuppressWarnings("unused")
public class Viewer {

    private StringProperty username;
    private IntegerProperty exp;
    private DoubleProperty time;

    public Viewer(String username, int exp, int time) {
        setUsername(username);
        setExp(exp);
        setTime(round((double) time / 60, 1));
    }

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    public void setUsername(String value) {
        usernameProperty().set(value);
    }

    public StringProperty usernameProperty() {
        if (username == null) username = new SimpleStringProperty(this, "username");
        return username;
    }

    public int getExp() {
        return expProperty().get();
    }

    public void setExp(int value) {
        expProperty().set(value);
    }

    public IntegerProperty expProperty() {
        if (exp == null) exp = new SimpleIntegerProperty(this, "exp");
        return exp;
    }

    public double getTime() {
        return timeProperty().get();
    }

    public void setTime(double value) {
        timeProperty().set(value);
    }

    public DoubleProperty timeProperty() {
        if (time == null) time = new SimpleDoubleProperty(this, "time");
        return time;
    }
}