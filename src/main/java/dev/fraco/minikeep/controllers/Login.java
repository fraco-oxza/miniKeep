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

import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private final Context ctx = Context.getInstance();
    public TextField emailField;
    public PasswordField passwordField;
    public HBox errorLabel;
    public Button loginButton;
    public Button signInButton;

    public void moveToSignIn(ActionEvent ignoredActionEvent) {
        Application.setRoot("sign-in");
    }

    public void tryToLogin(ActionEvent ignoredActionEvent) {
        User user = ctx.users.getUser(emailField.getText(), passwordField.getText());

        if (user != null) {
            ctx.setActualUser(user);

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
