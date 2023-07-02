package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.MiniKeepMain;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Login {
    private final Context ctx = Context.getInstance();
    public TextField emailField;
    public PasswordField passwordField;
    public HBox errorLabel;
    public Button loginButton;
    public Button signInButton;

    /**
     * Switches to the sign-in view.
     */
    public void moveToSignIn(ActionEvent ignoredActionEvent) {
        MiniKeepMain.setRoot("sign-in");
    }

    /**
     * Attempts to log in with the provided credentials.
     * If successful, sets the logged-in user in the context and switches to the workspace view.
     * Otherwise, displays an error message.
     */
    public void tryToLogin(ActionEvent ignoredActionEvent) {
        User user = ctx.users.getUser(emailField.getText(), passwordField.getText());

        if (user != null) {
            ctx.setActualUser(user);
            MiniKeepMain.setRoot("workspace");
        } else {
            errorLabel.setVisible(true);
        }
    }
}
