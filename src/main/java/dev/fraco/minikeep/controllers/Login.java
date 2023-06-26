package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField emailField;
    public PasswordField passwordField;
    public HBox errorLabel;
    public Button loginButton;
    public Button signInButton;

    public void moveToSignIn(ActionEvent actionEvent) throws IOException {
        Application.setRoot("sign-in");
    }

    public void tryToLogin(ActionEvent actionEvent) throws IOException {
        User user = Users.getInstance().getUser(emailField.getText(), passwordField.getText());

        if (user != null) {
            Context.getInstance().setActualUser(user);

            Application.setRoot("workspace");
        } else {
            errorLabel.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnMouseEntered(event-> {
            loginButton.setStyle("-fx-background-color: #cecece;");
            loginButton.setCursor(Cursor.HAND);
        });
        loginButton.setOnMouseExited(event-> {
            loginButton.setStyle("-fx-background-color: #fff;");
            loginButton.setCursor(Cursor.DEFAULT);
        });
        signInButton.setOnMouseEntered(event-> {
            signInButton.setTextFill(Paint.valueOf("A1E0D5"));
            signInButton.setCursor(Cursor.HAND);
        });
        signInButton.setOnMouseExited(event-> {
            signInButton.setTextFill(Paint.valueOf("#7daea3"));
            signInButton.setCursor(Cursor.DEFAULT);
        });
    }
}
