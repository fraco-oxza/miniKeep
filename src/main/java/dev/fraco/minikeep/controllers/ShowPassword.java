package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.Context;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowPassword implements Initializable {
    private final Context ctx = Context.getInstance();
    public Label password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.setText(ctx.getActualUser().getPassword());
        ctx.setActualUser(null);
    }

    public void nextHandler(ActionEvent actionEvent) {
        Application.setRoot("login");
    }
}
