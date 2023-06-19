package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.User;
import dev.fraco.minikeep.logic.Users;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Login {
    public TextField emailField;
    public PasswordField passwordField;

    public void moveToSignIn(ActionEvent actionEvent) {
    }

    public void tryToLogin(ActionEvent actionEvent) throws IOException {
        User user = Users.getInstance().getUser(emailField.getText(), passwordField.getText());

        if (user != null) {
            Context.getInstance().setActualUser(user);
            Application.setRoot("workspace");
        } else {
            System.out.println("Error en el login");
            Application.setRoot("login");
        }
    }
}
