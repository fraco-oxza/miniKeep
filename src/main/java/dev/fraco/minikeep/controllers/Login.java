package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login {
    public TextField emailField;
    public PasswordField passwordField;
    public HBox errorLabel;

    public void moveToSignIn(ActionEvent actionEvent) throws IOException {
        Application.setRoot("sign-in");
    }

    public void tryToLogin(ActionEvent actionEvent) throws IOException {
        User user = Users.getInstance().getUser(emailField.getText(), passwordField.getText());

        if (user != null) {
            Context.getInstance().setActualUser(user);
           new Note("Nota de cds" + user.getRegistrationNumber() + " 1", "Esta es una nota de prueba", new ArrayList<>(), "rojo", Priority.High, user);
            new Note("Nota de vads"+ user.getRegistrationNumber() + " 2", "Esta es una nota de prueba", new ArrayList<>(), "rojo", Priority.High, user);
            new Note("Nota de benja"+ user.getRegistrationNumber() + " 3", "Esta es una nota de prueba", new ArrayList<>(), "rojo", Priority.High, user);
            Application.setRoot("workspace");
        } else {
            errorLabel.setVisible(true);
        }
    }
}
